package com.jhc.fruit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="ai-api") //读取节点
@Data
public class AIConfig {
    private String aiCzm;
    private String aiZen;
}
