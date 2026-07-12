package com.technova.iam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.AccountEntitlement;

public interface AccountEntitlementRepository
        extends JpaRepository<AccountEntitlement, Long> {

    boolean existsByAccountIdAndEntitlementId(
            Long accountId,
            Long entitlementId
    );

    List<AccountEntitlement> findByAccountId(Long accountId);
}