package com.jhc.fruit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxConfig {
    
    private MiniApp miniapp = new MiniApp();
    
    @Data  // 内部类也加上 @Data 注解，自动生成 getter/setter
    public static class MiniApp {
        private String appid;
        private String secret;
        private String grantType = "authorization_code";  // 默认值
        private String apiUrl = "https://api.weixin.qq.com/sns/jscode2session";  // 默认值
    }
}