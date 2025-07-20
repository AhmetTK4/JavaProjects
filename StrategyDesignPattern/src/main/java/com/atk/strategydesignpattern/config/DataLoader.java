package com.atk.strategydesignpattern.config;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    public CommandLineRunner initDatabase(OrderRepository repository) {
        return args -> {
            repository.save(new Order("CREDIT_CARD", 100.0, "CREATED"));
            repository.save(new Order("PAYPAL", 200.0, "CREATED"));
        };
    }
}
