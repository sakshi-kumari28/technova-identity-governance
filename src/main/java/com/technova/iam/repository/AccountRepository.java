package com.technova.iam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technova.iam.entity.Account;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    boolean existsByAccountNameAndTargetApplicationId(
            String accountName,
            Long applicationId
    );

    boolean existsByAccountNameAndTargetApplicationIdAndIdNot(
            String accountName,
            Long applicationId,
            Long id
    );

    List<Account> findByEmployeeId(
            Long employeeId
    );

    Optional<Account> findByEmployeeIdAndTargetApplicationId(
            Long employeeId,
            Long applicationId
    );
}