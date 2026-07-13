package com.technova.iam.entity;

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
        name = "role_entitlements",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "business_role_id",
                                "entitlement_id"
                        }
                )
        }
)
public class RoleEntitlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "business_role_id",
            nullable = false
    )
    private BusinessRole businessRole;

    @ManyToOne
    @JoinColumn(
            name = "entitlement_id",
            nullable = false
    )
    private Entitlement entitlement;

    @Column(nullable = false)
    private boolean active;

    public RoleEntitlement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusinessRole getBusinessRole() {
        return businessRole;
    }

    public void setBusinessRole(BusinessRole businessRole) {
        this.businessRole = businessRole;
    }

    public Entitlement getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Entitlement entitlement) {
        this.entitlement = entitlement;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}