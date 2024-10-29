package com.example.proxydesignpattern.config;

import com.example.proxydesignpattern.entity.User;
import com.example.proxydesignpattern.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("adminUser", "admin", "admin@example.com"));
            userRepository.save(new User("regularUser", "user", "user@example.com"));
        };
    }
}


