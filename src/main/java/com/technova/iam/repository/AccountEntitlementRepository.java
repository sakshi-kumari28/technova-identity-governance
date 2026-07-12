package com.technova.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.AccountEntitlement;

public interface AccountEntitlementRepository
        extends JpaRepository<AccountEntitlement, Long> {

    boolean existsByAccountIdAndEntitlementId(
            Long accountId,
            Long entitlementId
    );
}