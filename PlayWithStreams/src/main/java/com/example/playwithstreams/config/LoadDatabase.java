package com.example.playwithstreams.config;

import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            repository.saveAll(List.of(
                    new Employee(null, "Alice", "HR", 6000),
                    new Employee(null, "Bob", "IT", 7000),
                    new Employee(null, "Charlie", "Sales", 4000),
                    new Employee(null, "David", "IT", 5000),
                    new Employee(null, "Eve", "Sales", 8000)
            ));
        };
    }
}
