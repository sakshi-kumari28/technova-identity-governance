package com.technova.iam.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technova.iam.dto.CreateLifecycleEventRequest;
import com.technova.iam.dto.MoverLifecycleRequest;
import com.technova.iam.entity.LifecycleEvent;
import com.technova.iam.service.LifecycleEventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lifecycle-events")
public class LifecycleEventController {

    private final LifecycleEventService lifecycleEventService;

    public LifecycleEventController(
            LifecycleEventService lifecycleEventService) {

        this.lifecycleEventService = lifecycleEventService;
    }

    @PostMapping
    public ResponseEntity<LifecycleEvent> createLifecycleEvent(
            @Valid @RequestBody CreateLifecycleEventRequest request) {

        LifecycleEvent lifecycleEvent =
                lifecycleEventService.createLifecycleEvent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lifecycleEvent);
    }

    @PostMapping("/mover")
    public ResponseEntity<LifecycleEvent> processMoverLifecycle(
            @Valid @RequestBody MoverLifecycleRequest request) {

        LifecycleEvent lifecycleEvent =
                lifecycleEventService.processMoverLifecycle(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lifecycleEvent);
    }

    @GetMapping
    public ResponseEntity<List<LifecycleEvent>> getAllLifecycleEvents() {

        List<LifecycleEvent> lifecycleEvents =
                lifecycleEventService.getAllLifecycleEvents();

        return ResponseEntity.ok(lifecycleEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LifecycleEvent> getLifecycleEventById(
            @PathVariable Long id) {

        LifecycleEvent lifecycleEvent =
                lifecycleEventService.getLifecycleEventById(id);

        return ResponseEntity.ok(lifecycleEvent);
    }
}