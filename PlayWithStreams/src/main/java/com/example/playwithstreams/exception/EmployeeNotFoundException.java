package com.example.playwithstreams.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Employee " + id + " not found.");
    }
}
