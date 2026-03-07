package com.jhc.fruit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "txt-config")
public class TxtConfig {

    private DescriptionConfig descriptionConfig;

    @Data
    public static class DescriptionConfig {
        private String title;
        private String email;
        private String version;
        private String description;
        private String name;
        private String url;
    }
}
