package com.example.playwithstreams.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springBootStreamAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Stream API")
                        .description("Spring Boot proje için Java Stream fonksiyonları API'si")
                        .version("v1.0")
                        .license(new License()
                                .name("MIT")
                                .url("https://github.com/AhmetTK4/JavaProjects/blob/main/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("Proje GitHub Reposu")
                        .url("https://github.com/AhmetTK4/JavaProjects/tree/main/PlayWithStreams"));
    }
}
