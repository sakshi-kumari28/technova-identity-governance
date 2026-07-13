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

import com.technova.iam.dto.CreateBusinessRoleRequest;
import com.technova.iam.entity.BusinessRole;
import com.technova.iam.service.BusinessRoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/business-roles")
public class BusinessRoleController {

    private final BusinessRoleService businessRoleService;

    public BusinessRoleController(
            BusinessRoleService businessRoleService) {

        this.businessRoleService = businessRoleService;
    }

    @PostMapping
    public ResponseEntity<BusinessRole> createBusinessRole(
            @Valid @RequestBody CreateBusinessRoleRequest request) {

        BusinessRole businessRole =
                businessRoleService.createBusinessRole(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(businessRole);
    }

    @GetMapping
    public ResponseEntity<List<BusinessRole>> getAllBusinessRoles() {

        List<BusinessRole> businessRoles =
                businessRoleService.getAllBusinessRoles();

        return ResponseEntity.ok(businessRoles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessRole> getBusinessRoleById(
            @PathVariable Long id) {

        BusinessRole businessRole =
                businessRoleService.getBusinessRoleById(id);

        return ResponseEntity.ok(businessRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessRole> updateBusinessRole(
            @PathVariable Long id,
            @Valid @RequestBody CreateBusinessRoleRequest request) {

        BusinessRole businessRole =
                businessRoleService.updateBusinessRole(id, request);

        return ResponseEntity.ok(businessRole);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<BusinessRole> deactivateBusinessRole(
            @PathVariable Long id) {

        BusinessRole businessRole =
                businessRoleService.deactivateBusinessRole(id);

        return ResponseEntity.ok(businessRole);
    }
}