package com.atk.strategydesignpattern.config;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.OrderStatus;
import com.atk.strategydesignpattern.entity.PaymentType;
import com.atk.strategydesignpattern.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {
    @Bean
    public CommandLineRunner initDatabase(OrderRepository repository) {
        return args -> {
            repository.save(new Order(PaymentType.CREDIT_CARD, new BigDecimal("100.00"), OrderStatus.CREATED));
            repository.save(new Order(PaymentType.PAYPAL, new BigDecimal("200.00"), OrderStatus.CREATED));
        };
    }
}
