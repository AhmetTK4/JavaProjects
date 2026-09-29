package com.atk.proxydesignpattern.config;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Seed only an empty table so restarts against a persistent database do not duplicate rows.
            if (userRepository.count() > 0) {
                return;
            }
            userRepository.save(new User("adminUser", "admin", "admin@example.com"));
            userRepository.save(new User("regularUser", "user", "user@example.com"));
        };
    }
}


