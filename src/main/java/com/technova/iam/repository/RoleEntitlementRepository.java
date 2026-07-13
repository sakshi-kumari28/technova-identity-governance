package com.technova.iam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.RoleEntitlement;

public interface RoleEntitlementRepository
        extends JpaRepository<RoleEntitlement, Long> {

    boolean existsByBusinessRoleIdAndEntitlementId(
            Long businessRoleId,
            Long entitlementId
    );

    List<RoleEntitlement> findByBusinessRoleId(
            Long businessRoleId
    );

    List<RoleEntitlement> findByBusinessRoleIdAndActiveTrue(
            Long businessRoleId
    );

    boolean existsByBusinessRoleIdAndEntitlementIdAndActiveTrue(
            Long businessRoleId,
            Long entitlementId
    );
}