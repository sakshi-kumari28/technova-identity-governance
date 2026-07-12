package com.technova.iam.dto;

import jakarta.validation.constraints.NotNull;

public class AssignEntitlementRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Entitlement ID is required")
    private Long entitlementId;

    public AssignEntitlementRequest() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getEntitlementId() {
        return entitlementId;
    }

    public void setEntitlementId(Long entitlementId) {
        this.entitlementId = entitlementId;
    }
}