package com.example.playwithstreams.controller;

import com.example.playwithstreams.dto.SalaryRange;
import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.service.EmployeeService;
import com.example.playwithstreams.stats.DepartmentReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Controller", description = "Çalışan verileri üzerinden stream işlemleri yapan API")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/high-salary")
    @Operation(summary = "Maaşı yüksek olan çalışanları getir",
            description = "Maaşı minSalary değerinden (varsayılan 5000) fazla olan çalışanları döndürür. filter + toList")
    public List<Employee> getHighSalaryEmployees(@RequestParam(defaultValue = "5000") BigDecimal minSalary) {
        return service.getHighSalaryEmployees(minSalary);
    }

    @GetMapping("/names-uppercase")
    @Operation(summary = "Çalışan isimlerini büyük harflerle döndür",
            description = "Tüm çalışan isimleri büyük harfe çevrilir. map + toList")
    public List<String> getEmployeeNamesInUppercase() {
        return service.getEmployeeNamesInUppercase();
    }

    @GetMapping("/group-by-department")
    @Operation(summary = "Çalışanları departmana göre gruplandır",
            description = "Tüm çalışanları departmanlarına göre gruplandırır. groupingBy")
    public Map<String, List<Employee>> groupByDepartment() {
        return service.groupByDepartment();
    }

    @GetMapping("/total-salary")
    @Operation(summary = "Toplam maaşı hesapla",
            description = "Tüm çalışanların maaşlarının toplamını döndürür. map + reduce")
    public BigDecimal getTotalSalary() {
        return service.getTotalSalary();
    }

    @GetMapping("/partition-by-salary")
    @Operation(summary = "Çalışanları maaşa göre ikiye ayır",
            description = "true: maaşı minSalary değerinden fazla olanlar, false: diğerleri. partitioningBy")
    public Map<Boolean, List<Employee>> partitionBySalary(@RequestParam(defaultValue = "5000") BigDecimal minSalary) {
        return service.partitionBySalary(minSalary);
    }

    @GetMapping("/average-salary-by-department")
    @Operation(summary = "Departman bazında ortalama maaş",
            description = "groupingBy + collectingAndThen")
    public Map<String, BigDecimal> averageSalaryByDepartment() {
        return service.averageSalaryByDepartment();
    }

    @GetMapping("/top-earner-by-department")
    @Operation(summary = "Her departmanın en yüksek maaşlı çalışanı",
            description = "toMap + BinaryOperator.maxBy")
    public Map<String, Employee> topEarnerByDepartment() {
        return service.topEarnerByDepartment();
    }

    @GetMapping("/salary-range")
    @Operation(summary = "En düşük ve en yüksek maaşlı çalışan",
            description = "Tek geçişte iki collector: teeing(minBy, maxBy)")
    public ResponseEntity<SalaryRange> salaryRange() {
        return ResponseEntity.of(service.salaryRange());
    }

    @GetMapping("/department-report")
    @Operation(summary = "Departman raporu",
            description = "Her departman ve şirket geneli için kişi sayısı, toplam/ortalama/min/max maaş ve isimler. "
                    + "Özel Collector + groupingBy + teeing, tek geçiş")
    public DepartmentReport departmentReport() {
        return service.departmentReport();
    }
}
