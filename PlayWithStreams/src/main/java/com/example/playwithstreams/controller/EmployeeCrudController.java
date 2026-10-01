package com.example.playwithstreams.controller;

import com.example.playwithstreams.dto.EmployeeRequest;
import com.example.playwithstreams.dto.PageResponse;
import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.service.EmployeeManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Management", description = "Çalışan ekleme, güncelleme, silme ve sayfalı listeleme")
public class EmployeeCrudController {

    private final EmployeeManagementService service;

    public EmployeeCrudController(EmployeeManagementService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Çalışanları sayfalı listele",
            description = "Parametreler: page (0'dan başlar), size (en fazla 100), sort=alan,asc|desc "
                    + "(id, name, department, salary), department (büyük/küçük harf duyarsız filtre)")
    public PageResponse<Employee> list(@RequestParam(required = false) String department,
                                       @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
                                       Pageable pageable) {
        return PageResponse.from(service.list(department, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Çalışanı ID ile getir")
    public Employee get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @Operation(summary = "Yeni çalışan ekle")
    public ResponseEntity<Employee> create(@Valid @RequestBody EmployeeRequest request) {
        Employee created = service.create(request);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Çalışanı güncelle")
    public Employee update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Çalışanı sil")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
