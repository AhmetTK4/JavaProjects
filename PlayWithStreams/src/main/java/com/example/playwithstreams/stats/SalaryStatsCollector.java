package com.example.playwithstreams.stats;

import com.example.playwithstreams.entity.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

/**
 * A custom {@link Collector} built with {@link Collector#of}: it computes headcount, total,
 * average, minimum and maximum salary and the sorted names in a single pass.
 * <p>
 * The four parts of a collector:
 * <ul>
 *   <li><b>supplier</b> creates an empty mutable {@link Accumulator},</li>
 *   <li><b>accumulator</b> adds one employee to it,</li>
 *   <li><b>combiner</b> merges two partial results, which parallel streams need,</li>
 *   <li><b>finisher</b> turns the accumulator into the immutable {@link SalaryStats}.</li>
 * </ul>
 * {@code DoubleSummaryStatistics} offers similar figures, but only for {@code double};
 * salaries are {@code BigDecimal} to stay exact.
 */
public final class SalaryStatsCollector {

    private SalaryStatsCollector() {
    }

    public static Collector<Employee, ?, SalaryStats> toSalaryStats() {
        return Collector.of(Accumulator::new, Accumulator::add, Accumulator::merge, Accumulator::finish);
    }

    static final class Accumulator {
        private long count;
        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal min;
        private BigDecimal max;
        private final List<String> names = new ArrayList<>();

        void add(Employee employee) {
            BigDecimal salary = employee.getSalary();
            count++;
            total = total.add(salary);
            min = min == null || salary.compareTo(min) < 0 ? salary : min;
            max = max == null || salary.compareTo(max) > 0 ? salary : max;
            names.add(employee.getName());
        }

        Accumulator merge(Accumulator other) {
            count += other.count;
            total = total.add(other.total);
            if (other.min != null && (min == null || other.min.compareTo(min) < 0)) {
                min = other.min;
            }
            if (other.max != null && (max == null || other.max.compareTo(max) > 0)) {
                max = other.max;
            }
            names.addAll(other.names);
            return this;
        }

        SalaryStats finish() {
            BigDecimal average = count == 0
                    ? BigDecimal.ZERO.setScale(2)
                    : total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
            return new SalaryStats(count, total, average, min, max, names.stream().sorted().toList());
        }
    }
}
