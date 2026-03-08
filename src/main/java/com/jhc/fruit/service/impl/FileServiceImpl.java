package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.R;
import com.jhc.fruit.config.MinioConfig;
import com.jhc.fruit.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
            String fileName = UUID.randomUUID().toString().replace("-" , "") + extFileName;

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



}
