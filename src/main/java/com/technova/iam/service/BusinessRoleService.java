package com.technova.iam.service;

import com.technova.iam.dto.CreateBusinessRoleRequest;
import com.technova.iam.entity.BusinessRole;
import com.technova.iam.exception.BusinessRoleNotFoundException;
import com.technova.iam.exception.DuplicateBusinessRoleException;
import com.technova.iam.repository.BusinessRoleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusinessRoleService {

    private final BusinessRoleRepository businessRoleRepository;

    public BusinessRoleService(
            BusinessRoleRepository businessRoleRepository) {

        this.businessRoleRepository = businessRoleRepository;
    }

    @Transactional
    public BusinessRole createBusinessRole(
            CreateBusinessRoleRequest request) {

        String roleName = request
                .getRoleName()
                .trim()
                .toUpperCase();

        if (businessRoleRepository.existsByRoleName(roleName)) {

            throw new DuplicateBusinessRoleException(
                    "Business role already exists with name: "
                            + roleName
            );
        }

        BusinessRole businessRole = new BusinessRole();

        businessRole.setRoleName(roleName);
        businessRole.setDescription(
                request.getDescription().trim()
        );
        businessRole.setActive(true);

        return businessRoleRepository.save(businessRole);
    }

    public List<BusinessRole> getAllBusinessRoles() {

        return businessRoleRepository.findAll();
    }

    public BusinessRole getBusinessRoleById(Long id) {

        return businessRoleRepository
                .findById(id)
                .orElseThrow(() ->
                        new BusinessRoleNotFoundException(
                                "Business role not found with id: " + id
                        )
                );
    }

    @Transactional
    public BusinessRole updateBusinessRole(
            Long id,
            CreateBusinessRoleRequest request) {

        BusinessRole businessRole =
                getBusinessRoleById(id);

        String roleName = request
                .getRoleName()
                .trim()
                .toUpperCase();

        if (businessRoleRepository
                .existsByRoleNameAndIdNot(roleName, id)) {

            throw new DuplicateBusinessRoleException(
                    "Business role already exists with name: "
                            + roleName
            );
        }

        businessRole.setRoleName(roleName);
        businessRole.setDescription(
                request.getDescription().trim()
        );

        return businessRoleRepository.save(businessRole);
    }

    @Transactional
    public BusinessRole deactivateBusinessRole(Long id) {

        BusinessRole businessRole =
                getBusinessRoleById(id);

        businessRole.setActive(false);

        return businessRoleRepository.save(businessRole);
    }
}