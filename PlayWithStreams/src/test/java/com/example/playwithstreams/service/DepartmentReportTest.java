package com.example.playwithstreams.service;

import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.repository.EmployeeRepository;
import com.example.playwithstreams.stats.DepartmentReport;
import com.example.playwithstreams.stats.SalaryStats;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DepartmentReportTest {

    private final EmployeeRepository repository = mock(EmployeeRepository.class);
    private final EmployeeService service = new EmployeeService(repository);

    private static Employee employee(String name, String department, String salary) {
        return new Employee(null, name, department, new BigDecimal(salary));
    }

    @Test
    void reportsEachDepartmentAndTheCompany() {
        when(repository.findAll()).thenReturn(List.of(
                employee("Alice", "HR", "6000"),
                employee("Bob", "IT", "7000"),
                employee("Charlie", "Sales", "4000"),
                employee("David", "IT", "5000"),
                employee("Eve", "Sales", "8000")));

        DepartmentReport report = service.departmentReport();

        assertEquals(List.of("HR", "IT", "Sales"), List.copyOf(report.departments().keySet()));
        SalaryStats it = report.departments().get("IT");
        assertEquals(2, it.headcount());
        assertEquals(new BigDecimal("12000"), it.totalSalary());
        assertEquals(new BigDecimal("6000.00"), it.averageSalary());
        assertEquals(List.of("Bob", "David"), it.employees());

        SalaryStats company = report.company();
        assertEquals(5, company.headcount());
        assertEquals(new BigDecimal("30000"), company.totalSalary());
        assertEquals(new BigDecimal("4000"), company.minSalary());
        assertEquals(new BigDecimal("8000"), company.maxSalary());
    }

    @Test
    void emptyDataGivesEmptyDepartmentsAndZeroCompany() {
        when(repository.findAll()).thenReturn(List.of());
        DepartmentReport report = service.departmentReport();
        assertTrue(report.departments().isEmpty());
        assertEquals(0, report.company().headcount());
    }
}
