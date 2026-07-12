package com.technova.iam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technova.iam.dto.AssignEntitlementRequest;
import com.technova.iam.entity.Account;
import com.technova.iam.entity.AccountEntitlement;
import com.technova.iam.entity.Entitlement;
import com.technova.iam.exception.AccountEntitlementNotFoundException;
import com.technova.iam.exception.AccountNotFoundException;
import com.technova.iam.exception.ApplicationMismatchException;
import com.technova.iam.exception.DuplicateAccountEntitlementException;
import com.technova.iam.exception.EntitlementNotFoundException;
import com.technova.iam.repository.AccountEntitlementRepository;
import com.technova.iam.repository.AccountRepository;
import com.technova.iam.repository.EntitlementRepository;

@Service
public class AccountEntitlementService {

    private final AccountEntitlementRepository accountEntitlementRepository;
    private final AccountRepository accountRepository;
    private final EntitlementRepository entitlementRepository;

    public AccountEntitlementService(
            AccountEntitlementRepository accountEntitlementRepository,
            AccountRepository accountRepository,
            EntitlementRepository entitlementRepository) {

        this.accountEntitlementRepository = accountEntitlementRepository;
        this.accountRepository = accountRepository;
        this.entitlementRepository = entitlementRepository;
    }

    public AccountEntitlement assignEntitlement(
            AssignEntitlementRequest request) {

        Account account = accountRepository
                .findById(request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: "
                                        + request.getAccountId()
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

        if (!account.getTargetApplication()
                .getId()
                .equals(entitlement.getTargetApplication().getId())) {

            throw new ApplicationMismatchException(
                    "Account and entitlement must belong to the same application"
            );
        }

        if (accountEntitlementRepository
                .existsByAccountIdAndEntitlementId(
                        request.getAccountId(),
                        request.getEntitlementId())) {

            throw new DuplicateAccountEntitlementException(
                    "Entitlement "
                            + request.getEntitlementId()
                            + " is already assigned to account "
                            + request.getAccountId()
            );
        }

        AccountEntitlement accountEntitlement =
                new AccountEntitlement();

        accountEntitlement.setAccount(account);
        accountEntitlement.setEntitlement(entitlement);
        accountEntitlement.setActive(true);

        return accountEntitlementRepository.save(accountEntitlement);
    }

    public List<AccountEntitlement> getAllAssignments() {

        return accountEntitlementRepository.findAll();
    }

    public AccountEntitlement getAssignmentById(Long id) {

        return accountEntitlementRepository
                .findById(id)
                .orElseThrow(() ->
                        new AccountEntitlementNotFoundException(
                                "Account entitlement assignment not found with id: "
                                        + id
                        )
                );
    }

    public void revokeEntitlement(Long id) {

        AccountEntitlement assignment = getAssignmentById(id);

        accountEntitlementRepository.delete(assignment);
    }
}