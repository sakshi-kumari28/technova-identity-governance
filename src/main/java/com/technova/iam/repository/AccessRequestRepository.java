package com.technova.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.AccessRequest;

public interface AccessRequestRepository
        extends JpaRepository<AccessRequest, Long> {

    boolean existsByAccountIdAndEntitlementIdAndStatus(
            Long accountId,
            Long entitlementId,
            String status
    );
}