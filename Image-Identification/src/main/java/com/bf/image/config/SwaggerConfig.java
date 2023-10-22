package com.bf.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : baofly
 * @version : 1.0
 */

@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                // 是否开启swagger
                .enable(true)
                .select()
                // 过滤条件，扫描指定路径下的文件
                .apis(RequestHandlerSelectors.basePackage("com.bf.image.controller"))
                // 指定路径处理，PathSelectors.any()代表不过滤任何路径
                //.paths(PathSelectors.any())
                .build();
    }

//    private ApiInfo apiInfo() {
//        /*作者信息*/
//        Contact contact = new Contact("baofly", "https://github.com/BaoFly/image-identification", "1787601154@qq.com");
//        return new ApiInfo(
//                "swagger接口文档",
//                "项目接口文档",
//                "v1.0",
//                "https://cunyu1943.github.io",
//                .termsOfServiceUrl("http://42.194.178.223/identification/") // 设置基本URL
//                contact,
//                "Apache 2.0",
//                "http://www.apache.org/licenses/LICENSE-2.0",
//                new ArrayList()
//        );

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("swagger接口文档")
                    .description("项目接口文档")
                    .version("1.0")
                    .termsOfServiceUrl("http://42.194.178.223/identification/") // 设置基本URL
                    .build();
        }
}