package com.technova.iam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technova.iam.dto.CreateEmployeeRequest;
import com.technova.iam.entity.Employee;
import com.technova.iam.entity.EmployeeStatus;
import com.technova.iam.exception.DuplicateEmployeeException;
import com.technova.iam.exception.EmployeeNotFoundException;
import com.technova.iam.repository.EmployeeRepository;

@Service
public class EmployeeService {
    public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
}

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
}

    public Employee createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmployeeNumber(request.getEmployeeNumber())) {
    throw new DuplicateEmployeeException(
            "Employee number already exists: " + request.getEmployeeNumber());
}

if (employeeRepository.existsByEmail(request.getEmail())) {
    throw new DuplicateEmployeeException(
            "Email already exists: " + request.getEmail());
}

        Employee employee = new Employee();

        employee.setEmployeeNumber(request.getEmployeeNumber());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setJobTitle(request.getJobTitle());

        employee.setStatus(EmployeeStatus.ACTIVE);

        return employeeRepository.save(employee);
    }
}