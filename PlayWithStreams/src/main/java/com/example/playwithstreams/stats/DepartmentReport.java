package com.example.playwithstreams.stats;

import java.util.Map;

/** Per-department statistics (sorted by department) plus the company-wide totals. */
public record DepartmentReport(Map<String, SalaryStats> departments, SalaryStats company) {
}
