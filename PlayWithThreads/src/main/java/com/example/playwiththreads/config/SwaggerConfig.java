package com.example.playwiththreads.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
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
                                .name("Ahmet Temel")
                                .email("your-email@gmail.com")
                                .url("https://example.com")));
    }
}

