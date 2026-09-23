package com.jhc.fruit.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ai-api") //??????
@Data
public class AIConfig {
    private String aiCzm;
    private String aiZen;
    private String aiAudio;
    private String aiCbh;
}
