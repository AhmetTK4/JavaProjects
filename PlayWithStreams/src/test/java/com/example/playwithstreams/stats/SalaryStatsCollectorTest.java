package com.example.playwithstreams.stats;

import com.example.playwithstreams.entity.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SalaryStatsCollectorTest {

    private static Employee employee(String name, String salary) {
        return new Employee(null, name, "IT", new BigDecimal(salary));
    }

    @Test
    void computesAllFiguresInOnePass() {
        SalaryStats stats = List.of(employee("Bob", "7000"), employee("David", "5000"), employee("Ann", "4000.10"))
                .stream().collect(SalaryStatsCollector.toSalaryStats());

        assertEquals(3, stats.headcount());
        assertEquals(new BigDecimal("16000.10"), stats.totalSalary());
        assertEquals(new BigDecimal("5333.37"), stats.averageSalary());
        assertEquals(new BigDecimal("4000.10"), stats.minSalary());
        assertEquals(new BigDecimal("7000"), stats.maxSalary());
        assertEquals(List.of("Ann", "Bob", "David"), stats.employees());
    }

    @Test
    void parallelStreamGivesTheSameResultAsSequential() {
        // Exercises the combiner: parallel streams merge partial accumulators.
        List<Employee> many = IntStream.rangeClosed(1, 10_000)
                .mapToObj(i -> employee("e" + i, i + ".25"))
                .toList();

        SalaryStats sequential = many.stream().collect(SalaryStatsCollector.toSalaryStats());
        SalaryStats parallel = many.parallelStream().collect(SalaryStatsCollector.toSalaryStats());

        assertEquals(sequential, parallel);
        assertEquals(new BigDecimal("1.25"), parallel.minSalary());
        assertEquals(new BigDecimal("10000.25"), parallel.maxSalary());
    }

    @Test
    void emptyGroupHasZeroTotalsAndNoMinMax() {
        SalaryStats stats = List.<Employee>of().stream().collect(SalaryStatsCollector.toSalaryStats());
        assertEquals(0, stats.headcount());
        assertEquals(BigDecimal.ZERO, stats.totalSalary());
        assertEquals(new BigDecimal("0.00"), stats.averageSalary());
        assertNull(stats.minSalary());
        assertNull(stats.maxSalary());
        assertTrue(stats.employees().isEmpty());
    }
}
