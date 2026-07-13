package com.technova.iam.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.technova.iam.entity.AuditEvent;
import com.technova.iam.repository.AuditEventRepository;

@Service
public class AuditEventService {

    private final AuditEventRepository auditEventRepository;

    public AuditEventService(
            AuditEventRepository auditEventRepository) {

        this.auditEventRepository = auditEventRepository;
    }

    @Transactional
    public AuditEvent createAuditEvent(
            String eventType,
            String entityType,
            Long entityId,
            String action,
            String description,
            String performedBy) {

        AuditEvent auditEvent = new AuditEvent();

        auditEvent.setEventType(eventType);
        auditEvent.setEntityType(entityType);
        auditEvent.setEntityId(entityId);
        auditEvent.setAction(action);
        auditEvent.setDescription(description);
        auditEvent.setPerformedBy(performedBy);

        return auditEventRepository.save(auditEvent);
    }

    public List<AuditEvent> getAllAuditEvents() {

        return auditEventRepository
                .findAllByOrderByEventTimestampDesc();
    }

    public List<AuditEvent> getAuditEventsByEventType(
            String eventType) {

        return auditEventRepository
                .findByEventType(eventType);
    }

    public List<AuditEvent> getAuditEventsByEntity(
            String entityType,
            Long entityId) {

        return auditEventRepository
                .findByEntityTypeAndEntityId(
                        entityType,
                        entityId
                );
    }
}