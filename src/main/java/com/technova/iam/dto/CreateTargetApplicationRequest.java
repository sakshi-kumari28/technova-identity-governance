package com.technova.iam.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTargetApplicationRequest {

    @NotBlank(message = "Application name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Application type is required")
    private String applicationType;

    public CreateTargetApplicationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }
}