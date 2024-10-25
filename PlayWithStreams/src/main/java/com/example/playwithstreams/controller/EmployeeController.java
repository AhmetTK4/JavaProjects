package com.example.playwithstreams.controller;

import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            description = "Maaşı 5000'den fazla olan çalışanların listesini döndürür.")
    public List<Employee> getHighSalaryEmployees() {
        return service.getHighSalaryEmployees();
    }

    @GetMapping("/names-uppercase")
    @Operation(summary = "Çalışan isimlerini büyük harflerle döndür",
            description = "Tüm çalışan isimleri büyük harfe çevrilir.")
    public List<String> getEmployeeNamesInUppercase() {
        return service.getEmployeeNamesInUppercase();
    }

    @GetMapping("/group-by-department")
    @Operation(summary = "Çalışanları departmana göre gruplandır",
            description = "Tüm çalışanları departmanlarına göre gruplandırır.")
    public Map<String, List<Employee>> groupByDepartment() {
        return service.groupByDepartment();
    }

    @GetMapping("/total-salary")
    @Operation(summary = "Toplam maaşı hesapla",
            description = "Tüm çalışanların maaşlarının toplamını döndürür.")
    public double getTotalSalary() {
        return service.getTotalSalary();
    }
}
