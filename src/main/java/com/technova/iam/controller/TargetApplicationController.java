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

import com.technova.iam.dto.CreateTargetApplicationRequest;
import com.technova.iam.dto.UpdateTargetApplicationRequest;
import com.technova.iam.entity.TargetApplication;
import com.technova.iam.service.TargetApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class TargetApplicationController {

    private final TargetApplicationService targetApplicationService;

    public TargetApplicationController(
            TargetApplicationService targetApplicationService) {

        this.targetApplicationService = targetApplicationService;
    }

    @PostMapping
    public ResponseEntity<TargetApplication> createApplication(
            @Valid @RequestBody CreateTargetApplicationRequest request) {

        TargetApplication application =
                targetApplicationService.createApplication(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(application);
    }

    @GetMapping
    public ResponseEntity<List<TargetApplication>> getAllApplications() {

        List<TargetApplication> applications =
                targetApplicationService.getAllApplications();

        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TargetApplication> getApplicationById(
            @PathVariable Long id) {

        TargetApplication application =
                targetApplicationService.getApplicationById(id);

        return ResponseEntity.ok(application);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TargetApplication> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTargetApplicationRequest request) {

        TargetApplication application =
                targetApplicationService.updateApplication(id, request);

        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long id) {

        targetApplicationService.deleteApplication(id);

        return ResponseEntity.noContent().build();
    }
}