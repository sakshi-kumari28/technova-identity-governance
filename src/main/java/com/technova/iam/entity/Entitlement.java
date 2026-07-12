package com.technova.iam.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "entitlements",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"entitlement_name", "application_id"}
                )
        }
)
public class Entitlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entitlement_name", nullable = false)
    private String entitlementName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String entitlementType;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private TargetApplication targetApplication;

    public Entitlement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TargetApplication getTargetApplication() {
        return targetApplication;
    }

    public void setTargetApplication(
            TargetApplication targetApplication) {

        this.targetApplication = targetApplication;
    }
}