package com.technova.iam.dto;

import jakarta.validation.constraints.NotNull;

public class CreateRoleEntitlementRequest {

    @NotNull(message = "Business role ID is required")
    private Long businessRoleId;

    @NotNull(message = "Entitlement ID is required")
    private Long entitlementId;

    public CreateRoleEntitlementRequest() {
    }

    public Long getBusinessRoleId() {
        return businessRoleId;
    }

    public void setBusinessRoleId(Long businessRoleId) {
        this.businessRoleId = businessRoleId;
    }

    public Long getEntitlementId() {
        return entitlementId;
    }

    public void setEntitlementId(Long entitlementId) {
        this.entitlementId = entitlementId;
    }
}