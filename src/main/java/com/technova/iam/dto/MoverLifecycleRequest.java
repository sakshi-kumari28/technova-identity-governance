package com.technova.iam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MoverLifecycleRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "New department is required")
    private String newDepartment;

    @NotBlank(message = "New job title is required")
    private String newJobTitle;

    @NotBlank(message = "Description is required")
    private String description;

    public MoverLifecycleRequest() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getNewDepartment() {
        return newDepartment;
    }

    public void setNewDepartment(String newDepartment) {
        this.newDepartment = newDepartment;
    }

    public String getNewJobTitle() {
        return newJobTitle;
    }

    public void setNewJobTitle(String newJobTitle) {
        this.newJobTitle = newJobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}