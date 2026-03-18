package com.jhc.fruit.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jhc.fruit.config.MinioConfig;
import com.jhc.fruit.service.FileService;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    //配置类由Spring注入
    @Autowired
    private MinioConfig minioProperties;

    //全局单例MinioClient
    private final MinioClient minioClient;

    //通过构造器注入 + @Autowired，保证MinioConfig加载完成后初始化MinioClient
    // （Spring会先注入minioProperties，再执行构造函数）
    @Autowired
    public FileServiceImpl(MinioConfig minioProperties) {
        this.minioProperties = minioProperties;
        // 初始化MinioClient（仅执行一次，全局复用）
        this.minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        log.info("MinioClient 单例初始化完成，Endpoint：{}", minioProperties.getEndpoint());
    }
    /**
     * 上传图片到minio
     * @param file
     * @return 返回图片地址
     */
    @Override
    public String upload(MultipartFile file) {
        try {

            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                log.info("Bucket '{}' not exists.create Now!", minioProperties.getBucketName());
            }

            // 设置存储对象名称
            String extFileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = "upload/" + UUID.randomUUID().toString().replace("-" , "") + extFileName;

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileName)
                    .build();
            minioClient.putObject(putObjectArgs) ;

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 预览文件：后端中转文件流，实现预览
     */
    public void preview(String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = getMinioFileInputStream(fileName);

            // 3. 处理文件名和MIME类型（用于预览响应头）
            String pureFileName = fileName.lastIndexOf("/") > 0 ? fileName.substring(fileName.lastIndexOf("/") + 1) : fileName;
            String ext = pureFileName.lastIndexOf(".") > 0 ? pureFileName.substring(pureFileName.lastIndexOf(".") + 1) : "";
            String contentType = "application/octet-stream";
            switch (ext.toLowerCase()) {
                case "png": contentType = "image/png"; break;
                case "jpg": case "jpeg": contentType = "image/jpeg"; break;
                case "gif": contentType = "image/gif"; break;
            }

            // 4. 设置预览响应头
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(pureFileName, StandardCharsets.UTF_8) + "\""); // 核心：预览

            // 5. 输出文件流到浏览器
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, len);
            }
            inputStream.close();
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("文件预览失败：" + e.getMessage(), e);
        }
    }

    /**
     * @param fileName
     * 获取图片数据流
     */
    public InputStream getMinioFileInputStream(String fileName){
        try {
            // 2. 获取文件流
            GetObjectArgs getArgs = GetObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(fileName)
                    .build();
            //返回图片数据流
            return minioClient.getObject(getArgs);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> uploads(List<MultipartFile> files) {
        try {

            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                log.info("Bucket '{}' not exists.create Now!", minioProperties.getBucketName());
            }

            List<String> paths = new ArrayList<>();

            for (MultipartFile file : files) {
                // 设置存储对象名称
                String extFileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = UUID.randomUUID().toString().replace("-" , "") + extFileName;

                PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .object(fileName)
                        .build();
                minioClient.putObject(putObjectArgs) ;

                paths.add(fileName);
            }


            return paths;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getImageUrl(String fileName) {
        try {
            //如果文件存在
            if (exits(fileName)){
                // 生成预签名URL
                String presignedUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(minioProperties.getBucketName())
                                .object(fileName)
                                .expiry(7, TimeUnit.DAYS) // URL过期时间
                                .build()
                );
                return presignedUrl;
            }
            //不存在
            return "文件不存在";
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return "获取图片URL失败";
        }
    }

    @Override
    public List<String> getImageUrls(List<String> files) {
        List<String> urls = new ArrayList<>();
        try {
            for (String fileName : files) {
                // 生成预签名URL
                String presignedUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(minioProperties.getBucketName())
                                .object(fileName)
                                .expiry(604800, TimeUnit.SECONDS) // URL过期时间,七天
                                .build()
                );
                urls.add(presignedUrl);
            }
            if (!urls.isEmpty()) {
                return urls;
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String save(MultipartFile file, String path) {
        try {

            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                log.info("Bucket '{}' not exists.create Now!", minioProperties.getBucketName());
            }

            // 设置存储对象名称
            String extFileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-" , "") + extFileName;
            String minioPath;
            if (path != null) {
                minioPath = path + "/" + fileName;
            }else {
                minioPath = fileName;
            }

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(minioPath)
                    .build();
            minioClient.putObject(putObjectArgs) ;

            return minioPath;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //删除文件
    public Boolean remove(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(fileName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //判断文件是否存在
    public Boolean exits(String fileName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(fileName)
                            .build()
            );
            //存在
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    // MinIO 过期时间参数名（固定）
    private static final String EXPIRIES_PARAM = "X-Amz-Expires";
    // MinIO 起始时间参数名（固定）
    private static final String DATE_PARAM = "X-Amz-Date";
    // MinIO 时间格式（固定）
    private static final DateTimeFormatter AMZ_DATE_FORMAT = DateTimeFormatter
            .ofPattern("yyyyMMdd'T'HHmmss'Z'")
            .withZone(ZoneId.of("UTC"));

    /**
     * 判断 MinIO 临时访问链接是否过期
     * @param presignedUrl MinIO 生成的 Presigned URL
     * @return true=已过期，false=未过期
     */
    public boolean isPresignedUrlExpired(String presignedUrl) {
        try {
            // 1. 校验入参
            if (StringUtils.isBlank(presignedUrl)) {
                throw new IllegalArgumentException("Presigned URL 不能为空");
            }

            // 2. 解析 URL，提取查询参数
            URI uri = new URI(presignedUrl);
            String query = uri.getQuery();
            if (StringUtils.isBlank(query)) {
                throw new IllegalArgumentException("Presigned URL 缺少查询参数");
            }

            // 3. 解析查询参数（key=value 形式）
            Map<String, String> paramMap = parseQueryParams(query);
            String expiresStr = paramMap.get(EXPIRIES_PARAM);
            String dateStr = paramMap.get(DATE_PARAM);

            // 4. 校验必要参数
            if (StringUtils.isBlank(expiresStr) || StringUtils.isBlank(dateStr)) {
                throw new IllegalArgumentException("Presigned URL 缺少过期时间相关参数");
            }

            // 5. 解析过期时长（秒）
            long expiresSeconds;
            try {
                expiresSeconds = Long.parseLong(expiresStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("X-Amz-Expires 参数格式错误：" + expiresStr, e);
            }

            // 6. 解析起始时间（转为时间戳，单位：秒）
            Instant startTime;
            try {
                startTime = Instant.from(AMZ_DATE_FORMAT.parse(dateStr));
            } catch (Exception e) {
                throw new IllegalArgumentException("X-Amz-Date 参数格式错误：" + dateStr, e);
            }

            // 7. 计算过期时间戳（秒）
            long expireTimestamp = startTime.getEpochSecond() + expiresSeconds;
            // 8. 获取当前时间戳（秒）
            long currentTimestamp = Instant.now().getEpochSecond();

            // 9. 对比判断是否过期
            return currentTimestamp > expireTimestamp;
        }catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            return true;
        }
    }

    /**
     * 解析 URL 查询参数为 Map
     */
    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> paramMap = new HashMap<>();
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=", 2); // 分割为key和value（避免value含=）
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8); // 解码URL编码的value
                paramMap.put(key, value);
            }
        }
        return paramMap;
    }

    /**
     * 解析MinIO临时访问URL，返回关键信息Map
     * @param presignedUrl MinIO生成的预签名URL（如：http://192.168.123.92:9000/my-bucket/test.jpg?X-Amz-Algorithm=...&X-Amz-Expires=3600...）
     * @return 解析结果Map，key包含：
     *         - endpoint: MinIO服务地址（如http://192.168.123.92:9000）
     *         - bucket: 存储桶名称
     *         - objectPath: 对象完整路径（URL解码后）
     *         - expiresSeconds: 过期时长（秒）
     *         - expireTime: 过期时间（Date类型，当前时间+过期时长）
     *         - queryParams: 所有URL查询参数（含签名参数）
     */
    public Map<String, Object> parsePresignedUrl(String presignedUrl) {
        try {
            // 1. 空值校验
            if (StrUtil.isBlank(presignedUrl)) {
                throw new IllegalArgumentException("MinIO预签名URL不能为空");
            }

            // 2. 解析URL基础结构
            URL url = new URL(presignedUrl);
            URI uri = new URI(presignedUrl);
            Map<String, Object> result = new HashMap<>();

            // 3. 解析MinIO服务地址（endpoint）
            String endpoint = url.getProtocol() + "://" + url.getHost() + (url.getPort() != -1 ? ":" + url.getPort() : "");
            result.put("endpoint", endpoint);

            // 4. 解析Bucket名称和对象路径
            String path = uri.getPath();
            if (StrUtil.isBlank(path) || path.equals("/")) {
                throw new IllegalArgumentException("URL路径为空，不符合MinIO预签名URL格式");
            }
            // 切割路径：/bucket/object/path → bucket=第一个部分，objectPath=剩余部分
            String[] pathParts = path.split("/", 3); // 按/切割为3部分，避免对象路径含/
            if (pathParts.length < 2) {
                throw new IllegalArgumentException("URL路径格式错误，无法解析Bucket和对象路径：" + path);
            }
            String bucket = pathParts[1]; // 第2部分是Bucket名（path以/开头，第1部分为空）
            String objectPath = pathParts.length == 3 ? pathParts[2] : "";
            // URL解码对象路径（处理空格、特殊字符等）
            objectPath = URLDecoder.decode(objectPath, StandardCharsets.UTF_8);
            result.put("bucket", bucket);
            result.put("objectPath", objectPath);

            // 5. 解析所有查询参数（含签名、过期时间等）
            String query = uri.getQuery();
            Map<String, String> queryParams = new HashMap<>();
            if (StrUtil.isNotBlank(query)) {
                String[] queryParts = query.split("&");
                for (String part : queryParts) {
                    String[] keyValue = part.split("=", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                        queryParams.put(key, value);
                    }
                }
            }
            result.put("queryParams", queryParams);

            // 6. 解析过期时长和过期时间（MinIO预签名URL的过期参数为X-Amz-Expires）
            String expiresSecondsStr = queryParams.get("X-Amz-Expires");
            if (StrUtil.isNotBlank(expiresSecondsStr)) {
                long expiresSeconds = Long.parseLong(expiresSecondsStr);
                result.put("expiresSeconds", expiresSeconds);
                // 计算过期时间（当前时间 + 过期时长）
                Date expireTime = DateUtil.offsetSecond(new Date(), (int) expiresSeconds);
                result.put("expireTime", expireTime);
            } else {
                result.put("expiresSeconds", null);
                result.put("expireTime", null);
            }

            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    /**
     * 从MinIO根据文件名获取文件，并转换为MultipartFile格式
     * @param fileName MinIO中存储的文件名（即upload方法返回的fileName）
     * @return MultipartFile 对象（MockMultipartFile实现）
     * @throws Exception 读取MinIO文件失败时抛出
     */
    @Override
    public MultipartFile getMinioFileAsMultipartFile(String fileName) {
        try {
            // 1. 修复文件路径：将所有反斜杠替换为正斜杠
            String fixedFileName = normalizePath(fileName);
            log.info("原始文件名: {}, 修复后: {}", fileName, fixedFileName);

            // 2. 校验文件是否存在
            if (!exits(fixedFileName)) {
                throw new RuntimeException("MinIO中不存在文件：" + fixedFileName);
            }

            // 3. 从MinIO读取文件流到字节数组
            InputStream minioInputStream = getMinioFileInputStream(fixedFileName);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192]; // 增大缓冲区提高性能
            int len;
            while ((len = minioInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            byte[] fileBytes = outputStream.toByteArray();

            // 4. 关闭流
            minioInputStream.close();
            outputStream.close();

            // 5. 提取安全的文件名（只取最后一部分，避免路径问题）
            String safeFileName = extractFileName(fixedFileName);
            String contentType = getContentTypeByFileName(safeFileName);

            // 6. 封装为MockMultipartFile
            return new MockMultipartFile(
                    "file",
                    safeFileName,  // 使用安全的文件名
                    contentType,
                    new ByteArrayInputStream(fileBytes)
            );

        } catch (Exception e) {
            log.error("获取MinIO文件失败: {}", fileName, e);
            throw new RuntimeException("获取MinIO文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 规范化文件路径：将所有反斜杠替换为正斜杠
     */
    private String normalizePath(String path) {
        if (path == null) return null;
        return path.replace('\\', '/');
    }

    /**
     * 从路径中提取安全的文件名
     */
    private String extractFileName(String path) {
        if (path == null) return null;

        // 清理路径分隔符
        String normalizedPath = normalizePath(path);

        // 获取最后一个'/'之后的部分
        int lastSlashIndex = normalizedPath.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < normalizedPath.length() - 1) {
            return normalizedPath.substring(lastSlashIndex + 1);
        }

        return normalizedPath;
    }

    /**
     * 批量从MinIO获取文件并转换为MultipartFile列表
     * @param fileNames MinIO文件名列表
     * @return MultipartFile列表
     * @throws Exception 读取失败时抛出
     */
    @Override
    public List<MultipartFile> getMinioFilesAsMultipartFileList(List<String> fileNames) throws Exception {
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (String fileName : fileNames) {
            multipartFiles.add(getMinioFileAsMultipartFile(fileName));
        }
        return multipartFiles;
    }

    /**
     * 根据文件名获取MIME类型（适配常见图片格式）
     */
    private String getContentTypeByFileName(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (ext) {
            case "png": return "image/png";
            case "jpg": case "jpeg": return "image/jpeg";
            case "gif": return "image/gif";
            case "bmp": return "image/bmp";
            default: return "application/octet-stream";
        }
    }

}
