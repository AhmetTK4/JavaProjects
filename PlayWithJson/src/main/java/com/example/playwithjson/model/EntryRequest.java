package com.example.playwithjson.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EntryRequest(@NotBlank @Size(max = 100) String name) {
}
