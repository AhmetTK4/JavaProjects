package com.example.playwiththreads.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kafka Spring Boot API")
                        .version("1.0")
                        .description("Kafka işlemleri için API dokümantasyonu")
                        .contact(new Contact()
                                .name("AhmetTK4")
                                .url("https://github.com/AhmetTK4/JavaProjects"))
                        .license(new License()
                                .name("MIT")
                                .url("https://github.com/AhmetTK4/JavaProjects/blob/main/LICENSE")));
    }
}

