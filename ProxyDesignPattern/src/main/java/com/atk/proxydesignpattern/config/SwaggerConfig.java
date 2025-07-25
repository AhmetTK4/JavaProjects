package com.atk.proxydesignpattern.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management API")
                        .version("1.0.0")
                        .description("Proxy Design Pattern ile geliştirilmiş kullanıcı yönetimi API'si")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
