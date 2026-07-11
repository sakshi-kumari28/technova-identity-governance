package com.technova.iam.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technova.iam.dto.CreateEmployeeRequest;
import com.technova.iam.entity.Employee;
import com.technova.iam.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping("/{id}")
public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

    Employee employee = employeeService.getEmployeeById(id);

    return ResponseEntity.ok(employee);
}

    @GetMapping
public ResponseEntity<List<Employee>> getAllEmployees() {
    List<Employee> employees = employeeService.getAllEmployees();
    return ResponseEntity.ok(employees);
}

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        Employee createdEmployee = employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdEmployee);
    }
}