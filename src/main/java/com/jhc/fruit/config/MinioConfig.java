package com.jhc.fruit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="minio") //读取节点
@Data
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
