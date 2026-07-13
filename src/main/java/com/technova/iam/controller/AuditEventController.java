package com.technova.iam.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technova.iam.entity.AuditEvent;
import com.technova.iam.service.AuditEventService;

@RestController
@RequestMapping("/api/audit-events")
public class AuditEventController {

    private final AuditEventService auditEventService;

    public AuditEventController(
            AuditEventService auditEventService) {

        this.auditEventService = auditEventService;
    }

    // =========================================================
    // GET ALL AUDIT EVENTS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<AuditEvent>> getAllAuditEvents() {

        List<AuditEvent> auditEvents =
                auditEventService.getAllAuditEvents();

        return ResponseEntity.ok(auditEvents);
    }

    // =========================================================
    // GET AUDIT EVENTS BY EVENT TYPE
    // =========================================================

    @GetMapping("/type/{eventType}")
    public ResponseEntity<List<AuditEvent>> getAuditEventsByEventType(
            @PathVariable String eventType) {

        List<AuditEvent> auditEvents =
                auditEventService
                        .getAuditEventsByEventType(eventType);

        return ResponseEntity.ok(auditEvents);
    }

    // =========================================================
    // GET AUDIT EVENTS BY ENTITY
    // =========================================================

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditEvent>> getAuditEventsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {

        List<AuditEvent> auditEvents =
                auditEventService
                        .getAuditEventsByEntity(
                                entityType,
                                entityId
                        );

        return ResponseEntity.ok(auditEvents);
    }
}