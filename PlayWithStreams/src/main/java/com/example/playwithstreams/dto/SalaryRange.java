package com.example.playwithstreams.dto;

import com.example.playwithstreams.entity.Employee;

public record SalaryRange(Employee lowest, Employee highest) {
}
