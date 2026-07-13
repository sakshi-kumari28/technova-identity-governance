package com.technova.iam.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.technova.iam.dto.CreateRoleEntitlementRequest;
import com.technova.iam.entity.BusinessRole;
import com.technova.iam.entity.Entitlement;
import com.technova.iam.entity.RoleEntitlement;
import com.technova.iam.exception.BusinessRoleNotFoundException;
import com.technova.iam.exception.DuplicateRoleEntitlementException;
import com.technova.iam.exception.EntitlementNotFoundException;
import com.technova.iam.exception.RoleEntitlementNotFoundException;
import com.technova.iam.repository.BusinessRoleRepository;
import com.technova.iam.repository.EntitlementRepository;
import com.technova.iam.repository.RoleEntitlementRepository;

@Service
public class RoleEntitlementService {

    private final RoleEntitlementRepository roleEntitlementRepository;
    private final BusinessRoleRepository businessRoleRepository;
    private final EntitlementRepository entitlementRepository;

    public RoleEntitlementService(
            RoleEntitlementRepository roleEntitlementRepository,
            BusinessRoleRepository businessRoleRepository,
            EntitlementRepository entitlementRepository) {

        this.roleEntitlementRepository = roleEntitlementRepository;
        this.businessRoleRepository = businessRoleRepository;
        this.entitlementRepository = entitlementRepository;
    }

    @Transactional
    public RoleEntitlement createRoleEntitlement(
            CreateRoleEntitlementRequest request) {

        BusinessRole businessRole = businessRoleRepository
                .findById(request.getBusinessRoleId())
                .orElseThrow(() ->
                        new BusinessRoleNotFoundException(
                                "Business role not found with id: "
                                        + request.getBusinessRoleId()
                        )
                );

        Entitlement entitlement = entitlementRepository
                .findById(request.getEntitlementId())
                .orElseThrow(() ->
                        new EntitlementNotFoundException(
                                "Entitlement not found with id: "
                                        + request.getEntitlementId()
                        )
                );

        if (!businessRole.isActive()) {

            throw new IllegalStateException(
                    "Cannot assign entitlement to inactive business role: "
                            + businessRole.getRoleName()
            );
        }

        if (!entitlement.isActive()) {

            throw new IllegalStateException(
                    "Cannot assign inactive entitlement: "
                            + entitlement.getEntitlementName()
            );
        }

        if (roleEntitlementRepository
                .existsByBusinessRoleIdAndEntitlementId(
                        request.getBusinessRoleId(),
                        request.getEntitlementId()
                )) {

            throw new DuplicateRoleEntitlementException(
                    "Entitlement "
                            + request.getEntitlementId()
                            + " is already mapped to business role "
                            + request.getBusinessRoleId()
            );
        }

        RoleEntitlement roleEntitlement =
                new RoleEntitlement();

        roleEntitlement.setBusinessRole(businessRole);
        roleEntitlement.setEntitlement(entitlement);
        roleEntitlement.setActive(true);

        return roleEntitlementRepository.save(roleEntitlement);
    }

    public List<RoleEntitlement> getAllRoleEntitlements() {

        return roleEntitlementRepository.findAll();
    }

    public RoleEntitlement getRoleEntitlementById(Long id) {

        return roleEntitlementRepository
                .findById(id)
                .orElseThrow(() ->
                        new RoleEntitlementNotFoundException(
                                "Role entitlement mapping not found with id: "
                                        + id
                        )
                );
    }

    public List<RoleEntitlement> getRoleEntitlementsByBusinessRole(
            Long businessRoleId) {

        if (!businessRoleRepository.existsById(businessRoleId)) {

            throw new BusinessRoleNotFoundException(
                    "Business role not found with id: "
                            + businessRoleId
            );
        }

        return roleEntitlementRepository
                .findByBusinessRoleId(businessRoleId);
    }

    @Transactional
    public RoleEntitlement deactivateRoleEntitlement(Long id) {

        RoleEntitlement roleEntitlement =
                getRoleEntitlementById(id);

        roleEntitlement.setActive(false);

        return roleEntitlementRepository.save(roleEntitlement);
    }
}