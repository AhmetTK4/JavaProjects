package com.atk.strategydesignpattern.config;

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
                        .title("Strategy Pattern API")
                        .version("1.0.0")
                        .description("Strategy Design Pattern example")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
