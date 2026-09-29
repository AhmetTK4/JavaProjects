package com.example.playwithgenerics.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record User(@NotBlank String name, @Min(0) @Max(150) int age) {
}
