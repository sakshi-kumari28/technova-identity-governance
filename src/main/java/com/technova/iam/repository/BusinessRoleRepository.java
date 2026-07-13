package com.technova.iam.repository;

import com.technova.iam.entity.BusinessRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRoleRepository
        extends JpaRepository<BusinessRole, Long> {

    boolean existsByRoleName(String roleName);

    boolean existsByRoleNameAndIdNot(
            String roleName,
            Long id
    );
}