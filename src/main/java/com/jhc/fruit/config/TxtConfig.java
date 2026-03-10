package com.jhc.fruit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="txt-config") //读取节点
@Data
public class TxtConfig {

    private DescriptionConfig descriptionConfig;
    private Filter filter;

    @Data
    public static class DescriptionConfig {
        private String title;
        private String email;
        private String version;
        private String description;
        private String name;
        private String url;
    }

    @Data
    public static class Filter {
        private Boolean flag;
    }
}
