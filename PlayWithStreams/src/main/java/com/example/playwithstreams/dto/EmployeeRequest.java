package com.example.playwithstreams.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EmployeeRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 50) String department,
        @NotNull @DecimalMin("0.01") @Digits(integer = 17, fraction = 2) BigDecimal salary) {
}
