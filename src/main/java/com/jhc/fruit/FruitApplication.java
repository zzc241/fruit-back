package com.jhc.fruit;

import com.jhc.fruit.config.AIConfig;
import com.jhc.fruit.config.TxtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
public class FruitApplication {

    public static void main(String[] args) {
        // 保存上下文对象
        ConfigurableApplicationContext context = SpringApplication.run(FruitApplication.class, args);

        // 注册JVM关闭钩子：手动触发上下文销毁（测试用，可临时添加）
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("开始关闭Spring上下文...");
            context.close();
            System.out.println("Spring上下文已关闭");
        }));
    }

}
