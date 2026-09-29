package com.example.playwithstreams.config;

import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            // Seed only an empty table so restarts against a persistent database do not duplicate rows.
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    new Employee(null, "Alice", "HR", new BigDecimal("6000")),
                    new Employee(null, "Bob", "IT", new BigDecimal("7000")),
                    new Employee(null, "Charlie", "Sales", new BigDecimal("4000")),
                    new Employee(null, "David", "IT", new BigDecimal("5000")),
                    new Employee(null, "Eve", "Sales", new BigDecimal("8000"))
            ));
        };
    }
}
