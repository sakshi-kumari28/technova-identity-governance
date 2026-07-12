package com.technova.iam.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technova.iam.dto.AssignEntitlementRequest;
import com.technova.iam.entity.AccountEntitlement;
import com.technova.iam.service.AccountEntitlementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/account-entitlements")
public class AccountEntitlementController {

    private final AccountEntitlementService accountEntitlementService;

    public AccountEntitlementController(
            AccountEntitlementService accountEntitlementService) {

        this.accountEntitlementService = accountEntitlementService;
    }

    @PostMapping
    public ResponseEntity<AccountEntitlement> assignEntitlement(
            @Valid @RequestBody AssignEntitlementRequest request) {

        AccountEntitlement assignment =
                accountEntitlementService.assignEntitlement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assignment);
    }

    @GetMapping
    public ResponseEntity<List<AccountEntitlement>> getAllAssignments() {

        List<AccountEntitlement> assignments =
                accountEntitlementService.getAllAssignments();

        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountEntitlement> getAssignmentById(
            @PathVariable Long id) {

        AccountEntitlement assignment =
                accountEntitlementService.getAssignmentById(id);

        return ResponseEntity.ok(assignment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeEntitlement(
            @PathVariable Long id) {

        accountEntitlementService.revokeEntitlement(id);

        return ResponseEntity.noContent().build();
    }
}