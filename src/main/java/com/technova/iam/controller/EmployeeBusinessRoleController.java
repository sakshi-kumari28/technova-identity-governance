package com.technova.iam.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technova.iam.dto.AssignBusinessRoleRequest;
import com.technova.iam.entity.EmployeeBusinessRole;
import com.technova.iam.service.EmployeeBusinessRoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employee-business-roles")
public class EmployeeBusinessRoleController {

    private final EmployeeBusinessRoleService employeeBusinessRoleService;

    public EmployeeBusinessRoleController(
            EmployeeBusinessRoleService employeeBusinessRoleService) {

        this.employeeBusinessRoleService = employeeBusinessRoleService;
    }

    @PostMapping
    public ResponseEntity<EmployeeBusinessRole> assignBusinessRole(
            @Valid @RequestBody AssignBusinessRoleRequest request) {

        EmployeeBusinessRole employeeBusinessRole =
                employeeBusinessRoleService.assignBusinessRole(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeBusinessRole);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeBusinessRole>>
            getAllEmployeeBusinessRoles() {

        List<EmployeeBusinessRole> assignments =
                employeeBusinessRoleService
                        .getAllEmployeeBusinessRoles();

        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeBusinessRole>
            getEmployeeBusinessRoleById(
                    @PathVariable Long id) {

        EmployeeBusinessRole assignment =
                employeeBusinessRoleService
                        .getEmployeeBusinessRoleById(id);

        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeBusinessRole>>
            getEmployeeBusinessRolesByEmployee(
                    @PathVariable Long employeeId) {

        List<EmployeeBusinessRole> assignments =
                employeeBusinessRoleService
                        .getEmployeeBusinessRolesByEmployee(employeeId);

        return ResponseEntity.ok(assignments);
    }

    @PutMapping("/{id}/revoke")
    public ResponseEntity<EmployeeBusinessRole> revokeBusinessRole(
            @PathVariable Long id) {

        EmployeeBusinessRole assignment =
                employeeBusinessRoleService
                        .revokeBusinessRole(id);

        return ResponseEntity.ok(assignment);
    }
}