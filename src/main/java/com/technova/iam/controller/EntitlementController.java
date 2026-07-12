package com.technova.iam.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technova.iam.dto.CreateEntitlementRequest;
import com.technova.iam.dto.UpdateEntitlementRequest;
import com.technova.iam.entity.Entitlement;
import com.technova.iam.service.EntitlementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/entitlements")
public class EntitlementController {

    private final EntitlementService entitlementService;

    public EntitlementController(
            EntitlementService entitlementService) {

        this.entitlementService = entitlementService;
    }

    @PostMapping
    public ResponseEntity<Entitlement> createEntitlement(
            @Valid @RequestBody CreateEntitlementRequest request) {

        Entitlement entitlement =
                entitlementService.createEntitlement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entitlement);
    }

    @GetMapping
    public ResponseEntity<List<Entitlement>> getAllEntitlements() {

        List<Entitlement> entitlements =
                entitlementService.getAllEntitlements();

        return ResponseEntity.ok(entitlements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entitlement> getEntitlementById(
            @PathVariable Long id) {

        Entitlement entitlement =
                entitlementService.getEntitlementById(id);

        return ResponseEntity.ok(entitlement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entitlement> updateEntitlement(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEntitlementRequest request) {

        Entitlement entitlement =
                entitlementService.updateEntitlement(id, request);

        return ResponseEntity.ok(entitlement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntitlement(
            @PathVariable Long id) {

        entitlementService.deleteEntitlement(id);

        return ResponseEntity.noContent().build();
    }
}