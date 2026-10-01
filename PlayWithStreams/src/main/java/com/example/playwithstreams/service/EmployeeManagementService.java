package com.example.playwithstreams.service;

import com.example.playwithstreams.dto.EmployeeRequest;
import com.example.playwithstreams.entity.Employee;
import com.example.playwithstreams.exception.EmployeeNotFoundException;
import com.example.playwithstreams.exception.InvalidSortException;
import com.example.playwithstreams.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * CRUD and paging. Unlike {@link EmployeeService}, which processes all rows in memory to show
 * the Stream API, paging and filtering here run in the database.
 */
@Service
public class EmployeeManagementService {

    static final Set<String> SORTABLE = Set.of("id", "name", "department", "salary");

    private final EmployeeRepository repository;

    public EmployeeManagementService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Employee> list(String department, Pageable pageable) {
        // Unknown sort properties would otherwise fail inside the query with a 500.
        for (Sort.Order order : pageable.getSort()) {
            if (!SORTABLE.contains(order.getProperty())) {
                throw new InvalidSortException(order.getProperty(), SORTABLE);
            }
        }
        return department == null || department.isBlank()
                ? repository.findAll(pageable)
                : repository.findByDepartmentIgnoreCase(department.trim(), pageable);
    }

    @Transactional(readOnly = true)
    public Employee get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional
    public Employee create(EmployeeRequest request) {
        return repository.save(new Employee(null, request.name().trim(), request.department().trim(), request.salary()));
    }

    @Transactional
    public Employee update(Long id, EmployeeRequest request) {
        Employee employee = get(id);
        employee.setName(request.name().trim());
        employee.setDepartment(request.department().trim());
        employee.setSalary(request.salary());
        return employee;
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
