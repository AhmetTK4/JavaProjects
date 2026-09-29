package com.example.playwithstreams.service;

import com.example.playwithstreams.dto.SalaryRange;
import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    private static final Employee ALICE = employee("Alice", "HR", "6000");
    private static final Employee BOB = employee("Bob", "IT", "7000");
    private static final Employee CHARLIE = employee("Charlie", "Sales", "4000");
    private static final Employee DAVID = employee("David", "IT", "5000");
    private static final Employee EVE = employee("Eve", "Sales", "8000.10");

    private final EmployeeRepository repository = mock(EmployeeRepository.class);
    private final EmployeeService service = new EmployeeService(repository);

    private static Employee employee(String name, String department, String salary) {
        return new Employee(null, name, department, new BigDecimal(salary));
    }

    @BeforeEach
    void setUp() {
        when(repository.findAll()).thenReturn(List.of(ALICE, BOB, CHARLIE, DAVID, EVE));
    }

    @Test
    void highSalaryUsesStrictThreshold() {
        assertEquals(List.of(ALICE, BOB, EVE), service.getHighSalaryEmployees(new BigDecimal("5000")));
        assertEquals(List.of(EVE), service.getHighSalaryEmployees(new BigDecimal("7000")));
    }

    @Test
    void totalSalaryIsExact() {
        // With double, sums like this can pick up binary rounding errors.
        assertEquals(new BigDecimal("30000.10"), service.getTotalSalary());
    }

    @Test
    void groupsAndPartitions() {
        Map<String, List<Employee>> groups = service.groupByDepartment();
        assertEquals(List.of("HR", "IT", "Sales"), List.copyOf(groups.keySet()));
        assertEquals(List.of(BOB, DAVID), groups.get("IT"));

        Map<Boolean, List<Employee>> partition = service.partitionBySalary(new BigDecimal("6000"));
        assertEquals(List.of(BOB, EVE), partition.get(true));
        assertEquals(List.of(ALICE, CHARLIE, DAVID), partition.get(false));
    }

    @Test
    void averageAndTopEarnerPerDepartment() {
        assertEquals(new BigDecimal("6000.00"), service.averageSalaryByDepartment().get("IT"));
        assertEquals(new BigDecimal("6000.05"), service.averageSalaryByDepartment().get("Sales"));
        assertEquals(BOB, service.topEarnerByDepartment().get("IT"));
        assertEquals(EVE, service.topEarnerByDepartment().get("Sales"));
    }

    @Test
    void salaryRangeUsesTeeing() {
        SalaryRange range = service.salaryRange().orElseThrow();
        assertEquals(CHARLIE, range.lowest());
        assertEquals(EVE, range.highest());
    }

    @Test
    void emptyDataGivesEmptyResults() {
        when(repository.findAll()).thenReturn(List.of());
        assertEquals(BigDecimal.ZERO, service.getTotalSalary());
        assertTrue(service.salaryRange().isEmpty());
        assertTrue(service.averageSalaryByDepartment().isEmpty());
    }
}
