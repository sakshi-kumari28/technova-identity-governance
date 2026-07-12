package com.technova.iam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateEntitlementRequest {

    @NotBlank(message = "Entitlement name is required")
    private String entitlementName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Entitlement type is required")
    private String entitlementType;

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    private boolean active;

    public UpdateEntitlementRequest() {
    }

    public String getEntitlementName() {
        return entitlementName;
    }

    public void setEntitlementName(String entitlementName) {
        this.entitlementName = entitlementName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntitlementType() {
        return entitlementType;
    }

    public void setEntitlementType(String entitlementType) {
        this.entitlementType = entitlementType;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}