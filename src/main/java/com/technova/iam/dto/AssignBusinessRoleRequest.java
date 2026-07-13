package com.technova.iam.dto;

import jakarta.validation.constraints.NotNull;

public class AssignBusinessRoleRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Business role ID is required")
    private Long businessRoleId;

    public AssignBusinessRoleRequest() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getBusinessRoleId() {
        return businessRoleId;
    }

    public void setBusinessRoleId(Long businessRoleId) {
        this.businessRoleId = businessRoleId;
    }
}