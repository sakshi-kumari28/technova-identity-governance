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

import com.technova.iam.dto.CreateRoleEntitlementRequest;
import com.technova.iam.entity.RoleEntitlement;
import com.technova.iam.service.RoleEntitlementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/role-entitlements")
public class RoleEntitlementController {

    private final RoleEntitlementService roleEntitlementService;

    public RoleEntitlementController(
            RoleEntitlementService roleEntitlementService) {

        this.roleEntitlementService = roleEntitlementService;
    }

    @PostMapping
    public ResponseEntity<RoleEntitlement> createRoleEntitlement(
            @Valid @RequestBody CreateRoleEntitlementRequest request) {

        RoleEntitlement roleEntitlement =
                roleEntitlementService.createRoleEntitlement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleEntitlement);
    }

    @GetMapping
    public ResponseEntity<List<RoleEntitlement>> getAllRoleEntitlements() {

        List<RoleEntitlement> roleEntitlements =
                roleEntitlementService.getAllRoleEntitlements();

        return ResponseEntity.ok(roleEntitlements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleEntitlement> getRoleEntitlementById(
            @PathVariable Long id) {

        RoleEntitlement roleEntitlement =
                roleEntitlementService.getRoleEntitlementById(id);

        return ResponseEntity.ok(roleEntitlement);
    }

    @GetMapping("/business-role/{businessRoleId}")
    public ResponseEntity<List<RoleEntitlement>>
            getRoleEntitlementsByBusinessRole(
                    @PathVariable Long businessRoleId) {

        List<RoleEntitlement> roleEntitlements =
                roleEntitlementService
                        .getRoleEntitlementsByBusinessRole(businessRoleId);

        return ResponseEntity.ok(roleEntitlements);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<RoleEntitlement> deactivateRoleEntitlement(
            @PathVariable Long id) {

        RoleEntitlement roleEntitlement =
                roleEntitlementService.deactivateRoleEntitlement(id);

        return ResponseEntity.ok(roleEntitlement);
    }
}