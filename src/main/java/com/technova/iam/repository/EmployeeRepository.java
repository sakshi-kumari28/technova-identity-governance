package com.technova.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeNumber(String employeeNumber);

    boolean existsByEmail(String email);
}