package com.technova.iam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAccessRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Entitlement ID is required")
    private Long entitlementId;

    @NotBlank(message = "Justification is required")
    private String justification;

    public CreateAccessRequest() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}