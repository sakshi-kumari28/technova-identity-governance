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

import com.technova.iam.dto.CreateAccessRequest;
import com.technova.iam.dto.ReviewAccessRequest;
import com.technova.iam.entity.AccessRequest;
import com.technova.iam.service.AccessRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/access-requests")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;

    public AccessRequestController(
            AccessRequestService accessRequestService) {

        this.accessRequestService = accessRequestService;
    }

    @PostMapping
    public ResponseEntity<AccessRequest> createAccessRequest(
            @Valid @RequestBody CreateAccessRequest request) {

        AccessRequest accessRequest =
                accessRequestService.createAccessRequest(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accessRequest);
    }

    @GetMapping
    public ResponseEntity<List<AccessRequest>> getAllAccessRequests() {

        List<AccessRequest> accessRequests =
                accessRequestService.getAllAccessRequests();

        return ResponseEntity.ok(accessRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessRequest> getAccessRequestById(
            @PathVariable Long id) {

        AccessRequest accessRequest =
                accessRequestService.getAccessRequestById(id);

        return ResponseEntity.ok(accessRequest);
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<AccessRequest> reviewAccessRequest(
            @PathVariable Long id,
            @Valid @RequestBody ReviewAccessRequest request) {

        AccessRequest accessRequest =
                accessRequestService.reviewAccessRequest(id, request);

        return ResponseEntity.ok(accessRequest);
    }
}