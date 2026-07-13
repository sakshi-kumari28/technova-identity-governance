package com.technova.iam.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateBusinessRoleRequest {

    @NotBlank(message = "Role name is required")
    private String roleName;

    @NotBlank(message = "Description is required")
    private String description;

    public CreateBusinessRoleRequest() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}