package com.technova.iam.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.technova.iam.dto.CreateAccessRequest;
import com.technova.iam.dto.ReviewAccessRequest;
import com.technova.iam.entity.AccessRequest;
import com.technova.iam.entity.Account;
import com.technova.iam.entity.AccountEntitlement;
import com.technova.iam.entity.Employee;
import com.technova.iam.entity.Entitlement;
import com.technova.iam.exception.AccessRequestAlreadyReviewedException;
import com.technova.iam.exception.AccessRequestNotFoundException;
import com.technova.iam.exception.AccountNotFoundException;
import com.technova.iam.exception.ApplicationMismatchException;
import com.technova.iam.exception.DuplicateAccessRequestException;
import com.technova.iam.exception.DuplicateAccountEntitlementException;
import com.technova.iam.exception.EmployeeNotFoundException;
import com.technova.iam.exception.EntitlementNotFoundException;
import com.technova.iam.exception.InvalidAccessRequestDecisionException;
import com.technova.iam.repository.AccessRequestRepository;
import com.technova.iam.repository.AccountEntitlementRepository;
import com.technova.iam.repository.AccountRepository;
import com.technova.iam.repository.EmployeeRepository;
import com.technova.iam.repository.EntitlementRepository;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final EntitlementRepository entitlementRepository;
    private final AccountEntitlementRepository accountEntitlementRepository;

    public AccessRequestService(
            AccessRequestRepository accessRequestRepository,
            EmployeeRepository employeeRepository,
            AccountRepository accountRepository,
            EntitlementRepository entitlementRepository,
            AccountEntitlementRepository accountEntitlementRepository) {

        this.accessRequestRepository = accessRequestRepository;
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.entitlementRepository = entitlementRepository;
        this.accountEntitlementRepository = accountEntitlementRepository;
    }

    public AccessRequest createAccessRequest(
            CreateAccessRequest request) {

        // Step 1: Validate employee
        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

        // Step 2: Validate account
        Account account = accountRepository
                .findById(request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: "
                                        + request.getAccountId()
                        )
                );

        // Step 3: Validate entitlement
        Entitlement entitlement = entitlementRepository
                .findById(request.getEntitlementId())
                .orElseThrow(() ->
                        new EntitlementNotFoundException(
                                "Entitlement not found with id: "
                                        + request.getEntitlementId()
                        )
                );

        // Step 4: Validate account ownership
        if (!account.getEmployee()
                .getId()
                .equals(employee.getId())) {

            throw new ApplicationMismatchException(
                    "Account does not belong to the specified employee"
            );
        }

        // Step 5: Validate application match
        if (!account.getTargetApplication()
                .getId()
                .equals(
                        entitlement.getTargetApplication().getId()
                )) {

            throw new ApplicationMismatchException(
                    "Account and entitlement must belong to the same application"
            );
        }

        // Step 6: Prevent duplicate PENDING requests
        if (accessRequestRepository
                .existsByAccountIdAndEntitlementIdAndStatus(
                        request.getAccountId(),
                        request.getEntitlementId(),
                        "PENDING")) {

            throw new DuplicateAccessRequestException(
                    "A pending access request already exists for account "
                            + request.getAccountId()
                            + " and entitlement "
                            + request.getEntitlementId()
            );
        }

        // Step 7: Create access request
        AccessRequest accessRequest = new AccessRequest();

        accessRequest.setEmployee(employee);
        accessRequest.setAccount(account);
        accessRequest.setEntitlement(entitlement);
        accessRequest.setJustification(request.getJustification());
        accessRequest.setStatus("PENDING");
        accessRequest.setRequestedAt(LocalDateTime.now());

        // Step 8: Save access request
        return accessRequestRepository.save(accessRequest);
    }

    public List<AccessRequest> getAllAccessRequests() {

        return accessRequestRepository.findAll();
    }

    public AccessRequest getAccessRequestById(Long id) {

        return accessRequestRepository
                .findById(id)
                .orElseThrow(() ->
                        new AccessRequestNotFoundException(
                                "Access request not found with id: " + id
                        )
                );
    }

    @Transactional
    public AccessRequest reviewAccessRequest(
            Long id,
            ReviewAccessRequest request) {

        // Step 1: Find access request
        AccessRequest accessRequest =
                getAccessRequestById(id);

        // Step 2: Prevent reviewing completed requests
        if (!"PENDING".equals(accessRequest.getStatus())) {

            throw new AccessRequestAlreadyReviewedException(
                    "Access request has already been reviewed with status: "
                            + accessRequest.getStatus()
            );
        }

        // Step 3: Normalize decision
        String decision = request
                .getDecision()
                .trim()
                .toUpperCase();

        // Step 4: Validate decision
        if (!"APPROVED".equals(decision)
                && !"REJECTED".equals(decision)) {

            throw new InvalidAccessRequestDecisionException(
                    "Decision must be APPROVED or REJECTED"
            );
        }

        // Step 5: Provision access when approved
        if ("APPROVED".equals(decision)) {

            Long accountId =
                    accessRequest.getAccount().getId();

            Long entitlementId =
                    accessRequest.getEntitlement().getId();

            // Prevent duplicate assignment
            if (accountEntitlementRepository
                    .existsByAccountIdAndEntitlementId(
                            accountId,
                            entitlementId)) {

                throw new DuplicateAccountEntitlementException(
                        "Entitlement "
                                + entitlementId
                                + " is already assigned to account "
                                + accountId
                );
            }

            AccountEntitlement assignment =
                    new AccountEntitlement();

            assignment.setAccount(accessRequest.getAccount());
            assignment.setEntitlement(accessRequest.getEntitlement());
            assignment.setActive(true);

            accountEntitlementRepository.save(assignment);
        }

        // Step 6: Update review information
        accessRequest.setStatus(decision);
        accessRequest.setReviewedAt(LocalDateTime.now());
        accessRequest.setReviewComment(request.getReviewComment());

        // Step 7: Save updated request
        return accessRequestRepository.save(accessRequest);
    }
}