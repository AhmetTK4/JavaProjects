package com.example.playwithstreams.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
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
                        .contact(new Contact()
                                .name("Temel")
                                .email("temel@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Proje GitHub Reposu")
                        .url("https://github.com/temel/stream-api-example"));
    }
}
