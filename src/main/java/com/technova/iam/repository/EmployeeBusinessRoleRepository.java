package com.technova.iam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.EmployeeBusinessRole;

public interface EmployeeBusinessRoleRepository
        extends JpaRepository<EmployeeBusinessRole, Long> {

    Optional<EmployeeBusinessRole> findByEmployeeIdAndBusinessRoleId(
            Long employeeId,
            Long businessRoleId
    );

    List<EmployeeBusinessRole> findByEmployeeId(
            Long employeeId
    );

    List<EmployeeBusinessRole> findByEmployeeIdAndActiveTrue(
            Long employeeId
    );

    List<EmployeeBusinessRole> findByBusinessRoleId(
            Long businessRoleId
    );
}
