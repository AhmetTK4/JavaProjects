package com.example.playwithstreams.repository;

import com.example.playwithstreams.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
