package com.example.playwithstreams.service;

import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }


    public List<Employee> getHighSalaryEmployees() {
        return repository.findAll().stream()
                .filter(emp -> emp.getSalary() > 5000)
                .collect(Collectors.toList());
    }


    public List<String> getEmployeeNamesInUppercase() {
        return repository.findAll().stream()
                .map(emp -> emp.getName().toUpperCase())
                .collect(Collectors.toList());
    }


    public Map<String, List<Employee>> groupByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }


    public double getTotalSalary() {
        return repository.findAll().parallelStream()
                .mapToDouble(Employee::getSalary)
                .sum();
    }
}
