package com.jhc.fruit.config;

import java.util.List;

import javax.lang.model.type.WildcardType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.classmate.TypeResolver;
import com.jhc.fruit.common.R;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.Contact; // 修改导入的Contact类

/**
 * @author ：lcy
 * @description：Swagger文档配置类
 * @date ：2022/5/9 0009 19:01
 */
@Configuration //将这个类在Spring中标注为配置类
@EnableSwagger2 //启用Swagger2
public class SwaggerConfig {
    @Autowired
    private TypeResolver typeResolver;
    @Autowired
    private TxtConfig txtConfig;
    //返回一个Docket Bean即可
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jhc.fruit.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                // 添加泛型模型支持
                .alternateTypeRules(
                        // 处理 R<List<T>>
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(R.class, 
                                        typeResolver.resolve(List.class, WildcardType.class)),
                                typeResolver.resolve(List.class, WildcardType.class)
                        ),
                        // 处理 R<Page<T>>
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(R.class, 
                                        typeResolver.resolve(Page.class, WildcardType.class)),
                                typeResolver.resolve(Page.class, WildcardType.class)
                        ),
                        // 处理 R<T> 其中 T 是普通类型
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(R.class, WildcardType.class),
                                typeResolver.resolve(WildcardType.class)
                        )
                )
                // 忽略泛型参数的警告
                .forCodeGeneration(true)
                .useDefaultResponseMessages(false);
                

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(txtConfig.getDescriptionConfig().getTitle())
                .description(txtConfig.getDescriptionConfig().getDescription())
                .version(txtConfig.getDescriptionConfig().getVersion())
                .contact(new Contact(txtConfig.getDescriptionConfig().getName(), txtConfig.getDescriptionConfig().getUrl(), txtConfig.getDescriptionConfig().getEmail()))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

}
