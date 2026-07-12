package com.technova.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.Entitlement;

public interface EntitlementRepository
        extends JpaRepository<Entitlement, Long> {

    boolean existsByEntitlementNameAndTargetApplicationId(
            String entitlementName,
            Long applicationId
    );

    boolean existsByEntitlementNameAndTargetApplicationIdAndIdNot(
            String entitlementName,
            Long applicationId,
            Long id
    );
}