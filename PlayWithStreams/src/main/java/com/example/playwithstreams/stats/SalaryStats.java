package com.example.playwithstreams.stats;

import java.math.BigDecimal;
import java.util.List;

/**
 * Summary of a group of employees. {@code minSalary} and {@code maxSalary} are {@code null}
 * for an empty group.
 */
public record SalaryStats(long headcount, BigDecimal totalSalary, BigDecimal averageSalary,
                          BigDecimal minSalary, BigDecimal maxSalary, List<String> employees) {
}
