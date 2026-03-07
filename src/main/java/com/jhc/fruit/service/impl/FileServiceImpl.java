package com.jhc.fruit.service.impl;

import com.jhc.fruit.common.R;
import com.jhc.fruit.config.MinioConfig;
import com.jhc.fruit.service.FileService;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioConfig minioProperties;

    /**
     * 上传图片到minio
     * @param file
     * @return 返回图片地址
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            // 创建一个Minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

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
            // 1. 创建MinIO客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

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
            // 创建一个Minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

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
            // 创建一个Minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

            // 生成预签名URL，有效期30分钟（可根据需求调整）
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioProperties.getBucketName())
                            .object(fileName)
                            .expiry(30, TimeUnit.MINUTES) // URL过期时间
                            .build()
            );
            return presignedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取图片URL失败：" + e.getMessage();
        }
    }

    @Override
    public List<String> getImageUrls(List<String> files) {
        List<String> urls = new ArrayList<>();
        try {
            // 创建一个Minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

            for (String fileName : files) {
                // 生成预签名URL，有效期30分钟
                String presignedUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(minioProperties.getBucketName())
                                .object(fileName)
                                .expiry(30, TimeUnit.MINUTES) // URL过期时间
                                .build()
                );
                urls.add(presignedUrl);
            }
            if (urls.size() > 0) {
                return urls;
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
