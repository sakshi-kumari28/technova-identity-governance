package com.technova.iam.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "employee_business_roles",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "employee_id",
                                "business_role_id"
                        }
                )
        }
)
public class EmployeeBusinessRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    private Employee employee;

    @ManyToOne
    @JoinColumn(
            name = "business_role_id",
            nullable = false
    )
    private BusinessRole businessRole;

    @Column(nullable = false)
    private boolean active;

    @Column(
            name = "assigned_at",
            nullable = false
    )
    private LocalDateTime assignedAt;

    private LocalDateTime revokedAt;

    public EmployeeBusinessRole() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public BusinessRole getBusinessRole() {
        return businessRole;
    }

    public void setBusinessRole(BusinessRole businessRole) {
        this.businessRole = businessRole;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }
}