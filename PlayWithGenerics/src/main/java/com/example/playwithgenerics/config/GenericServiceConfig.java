package com.example.playwithgenerics.config;



import com.example.playwithgenerics.model.User;
import com.example.playwithgenerics.service.GenericService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenericServiceConfig {

    @Bean
    public GenericService<String> stringService() {
        return new GenericService<>();
    }

    @Bean
    public GenericService<User> userService() {
        return new GenericService<>();
    }
}
