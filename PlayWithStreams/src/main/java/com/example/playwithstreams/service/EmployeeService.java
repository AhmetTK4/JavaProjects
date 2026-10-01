package com.example.playwithstreams.service;

import com.example.playwithstreams.dto.SalaryRange;
import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.repository.EmployeeRepository;
import com.example.playwithstreams.stats.DepartmentReport;
import com.example.playwithstreams.stats.SalaryStatsCollector;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stream API examples. The data is loaded with findAll() and processed in memory on purpose;
 * in a real application, filtering such as "salary greater than" belongs in a repository query.
 */
@Service
public class EmployeeService {

    private static final Comparator<Employee> BY_SALARY = Comparator.comparing(Employee::getSalary);

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    /** filter + toList */
    public List<Employee> getHighSalaryEmployees(BigDecimal minSalary) {
        return repository.findAll().stream()
                .filter(emp -> emp.getSalary().compareTo(minSalary) > 0)
                .toList();
    }

    /** map + toList */
    public List<String> getEmployeeNamesInUppercase() {
        return repository.findAll().stream()
                .map(emp -> emp.getName().toUpperCase())
                .toList();
    }

    /** groupingBy into a TreeMap so departments are sorted */
    public Map<String, List<Employee>> groupByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new, Collectors.toList()));
    }

    /**
     * map + reduce. BigDecimal has no sum() collector, so reduce with an identity and an accumulator.
     * A sequential stream is used: for a handful of elements a parallel stream only adds overhead.
     */
    public BigDecimal getTotalSalary() {
        return repository.findAll().stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** partitioningBy: always returns both {@code true} and {@code false} keys */
    public Map<Boolean, List<Employee>> partitionBySalary(BigDecimal minSalary) {
        return repository.findAll().stream()
                .collect(Collectors.partitioningBy(emp -> emp.getSalary().compareTo(minSalary) > 0));
    }

    /** groupingBy with a downstream collector built from collectingAndThen */
    public Map<String, BigDecimal> averageSalaryByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), EmployeeService::averageSalary)));
    }

    /** toMap with a merge function that keeps the higher-paid employee */
    public Map<String, Employee> topEarnerByDepartment() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(Employee::getDepartment, Function.identity(),
                        BinaryOperator.maxBy(BY_SALARY), TreeMap::new));
    }

    /** teeing: two collectors over one pass, combined into a record */
    public Optional<SalaryRange> salaryRange() {
        return repository.findAll().stream()
                .collect(Collectors.teeing(
                        Collectors.minBy(BY_SALARY),
                        Collectors.maxBy(BY_SALARY),
                        (min, max) -> min.map(lowest -> new SalaryRange(lowest, max.orElseThrow()))));
    }

    /**
     * One pass over the data with two consumers: {@code teeing} feeds every employee both to a
     * {@code groupingBy} that applies the custom collector per department and to the same custom
     * collector for the whole company.
     */
    public DepartmentReport departmentReport() {
        return repository.findAll().stream()
                .collect(Collectors.teeing(
                        Collectors.groupingBy(Employee::getDepartment, TreeMap::new, SalaryStatsCollector.toSalaryStats()),
                        SalaryStatsCollector.toSalaryStats(),
                        DepartmentReport::new));
    }

    private static BigDecimal averageSalary(List<Employee> employees) {
        BigDecimal total = employees.stream().map(Employee::getSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(employees.size()), 2, RoundingMode.HALF_UP);
    }
}
