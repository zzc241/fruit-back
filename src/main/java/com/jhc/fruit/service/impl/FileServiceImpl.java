package com.jhc.fruit.service.impl;

import com.jhc.fruit.config.MinioConfig;
import com.jhc.fruit.service.FileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioConfig minioProperties;

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

            return minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + fileName ;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 预览文件：后端中转文件流，实现预览
     */
    public void preview(String fileName, HttpServletResponse response) {
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
            InputStream inputStream = minioClient.getObject(getArgs);

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

}
