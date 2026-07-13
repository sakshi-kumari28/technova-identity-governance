package com.technova.iam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.AuditEvent;

public interface AuditEventRepository
        extends JpaRepository<AuditEvent, Long> {

    List<AuditEvent> findByEventType(
            String eventType
    );

    List<AuditEvent> findByEntityTypeAndEntityId(
            String entityType,
            Long entityId
    );

    List<AuditEvent> findAllByOrderByEventTimestampDesc();
}