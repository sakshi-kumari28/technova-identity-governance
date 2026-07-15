package com.technova.iam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final AuditEventService auditEventService;

    public AccessRequestService(
            AccessRequestRepository accessRequestRepository,
            EmployeeRepository employeeRepository,
            AccountRepository accountRepository,
            EntitlementRepository entitlementRepository,
            AccountEntitlementRepository accountEntitlementRepository,
            AuditEventService auditEventService) {

        this.accessRequestRepository = accessRequestRepository;
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.entitlementRepository = entitlementRepository;
        this.accountEntitlementRepository =
                accountEntitlementRepository;
        this.auditEventService = auditEventService;
    }

    // =========================================================
    // CREATE ACCESS REQUEST
    // =========================================================

    @Transactional
    public AccessRequest createAccessRequest(
            CreateAccessRequest request) {

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

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

        // Validate account ownership
        if (!account.getEmployee()
                .getId()
                .equals(employee.getId())) {

            throw new ApplicationMismatchException(
                    "Account does not belong to the specified employee"
            );
        }

        // Validate application match
        if (!account.getTargetApplication()
                .getId()
                .equals(
                        entitlement
                                .getTargetApplication()
                                .getId()
                )) {

            throw new ApplicationMismatchException(
                    "Account and entitlement must belong "
                            + "to the same application"
            );
        }

        // Prevent duplicate pending requests
        if (accessRequestRepository
                .existsByAccountIdAndEntitlementIdAndStatus(
                        account.getId(),
                        entitlement.getId(),
                        "PENDING"
                )) {

            throw new DuplicateAccessRequestException(
                    "A pending access request already exists "
                            + "for account "
                            + account.getId()
                            + " and entitlement "
                            + entitlement.getId()
            );
        }

        AccessRequest accessRequest =
                new AccessRequest();

        accessRequest.setEmployee(employee);
        accessRequest.setAccount(account);
        accessRequest.setEntitlement(entitlement);
        accessRequest.setJustification(
                request.getJustification()
        );
        accessRequest.setStatus("PENDING");
        accessRequest.setRequestedAt(
                LocalDateTime.now()
        );

        accessRequest =
                accessRequestRepository.save(
                        accessRequest
                );

        // =====================================================
        // AUDIT: REQUEST CREATED
        // =====================================================

      auditEventService.createAuditEvent(
        "ACCESS_REQUEST",
        "ACCESS_REQUEST",
        accessRequest.getId(),
        "ACCESS_REQUEST_REJECTED",
        "Access request "
                + accessRequest.getId()
                + " rejected for entitlement "
                + accessRequest
                        .getEntitlement()
                        .getEntitlementName(),
        "SYSTEM"
         );

        return accessRequest;
    }

    // =========================================================
    // GET ALL ACCESS REQUESTS
    // =========================================================

    public List<AccessRequest> getAllAccessRequests() {

        return accessRequestRepository.findAll();
    }

    // =========================================================
    // GET ACCESS REQUEST BY ID
    // =========================================================

    public AccessRequest getAccessRequestById(
            Long id) {

        return accessRequestRepository
                .findById(id)
                .orElseThrow(() ->
                        new AccessRequestNotFoundException(
                                "Access request not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // REVIEW ACCESS REQUEST
    // =========================================================

    @Transactional
    public AccessRequest reviewAccessRequest(
            Long id,
            ReviewAccessRequest request) {

        AccessRequest accessRequest =
                getAccessRequestById(id);

        if (!"PENDING".equals(
                accessRequest.getStatus())) {

            throw new AccessRequestAlreadyReviewedException(
                    "Access request has already been "
                            + "reviewed with status: "
                            + accessRequest.getStatus()
            );
        }

        String decision =
                request
                        .getDecision()
                        .trim()
                        .toUpperCase();

        if (!"APPROVED".equals(decision)
                && !"REJECTED".equals(decision)) {

            throw new InvalidAccessRequestDecisionException(
                    "Decision must be APPROVED or REJECTED"
            );
        }

        // =====================================================
        // APPROVED
        // =====================================================

        if ("APPROVED".equals(decision)) {

            provisionAccess(accessRequest);
        }

        // =====================================================
        // UPDATE REQUEST
        // =====================================================

        accessRequest.setStatus(decision);

        accessRequest.setReviewedAt(
                LocalDateTime.now()
        );

        accessRequest.setReviewComment(
                request.getReviewComment()
        );

        accessRequest =
                accessRequestRepository.save(
                        accessRequest
                );

        // =====================================================
        // AUDIT: REVIEW DECISION
        // =====================================================

        if ("APPROVED".equals(decision)) {

            auditEventService.createAuditEvent(
        "ACCESS_REQUEST",
        "ACCESS_REQUEST",
        accessRequest.getId(),
        "ACCESS_REQUEST_REJECTED",
        "Access request "
                + accessRequest.getId()
                + " rejected for entitlement "
                + accessRequest
                        .getEntitlement()
                        .getEntitlementName(),
        "SYSTEM"
);

        } else {

            auditEventService.createAuditEvent(
                    "ACCESS_REQUEST",
                    "ACCESS_REQUEST",
                    accessRequest.getId(),
                    "ACCESS_REQUEST_REJECTED",
                    "Access request "
                            + accessRequest.getId()
                            + " rejected for entitlement "
                            + accessRequest
                                    .getEntitlement()
                                    .getEntitlementName(),
                    "SYSTEM"
            );
        }

        return accessRequest;
    }

    // =========================================================
    // PROVISION ACCESS
    // =========================================================

    private void provisionAccess(
            AccessRequest accessRequest) {

        Account account =
                accessRequest.getAccount();

        Entitlement entitlement =
                accessRequest.getEntitlement();

        Optional<AccountEntitlement>
                existingAssignment =

                accountEntitlementRepository
                        .findByAccountIdAndEntitlementId(
                                account.getId(),
                                entitlement.getId()
                        );

        // =====================================================
        // EXISTING ASSIGNMENT
        // =====================================================

        if (existingAssignment.isPresent()) {

            AccountEntitlement accountEntitlement =
                    existingAssignment.get();

            if (accountEntitlement.isActive()) {

                throw new DuplicateAccountEntitlementException(
                        "Entitlement "
                                + entitlement.getId()
                                + " is already active for account "
                                + account.getId()
                );
            }

            accountEntitlement.setActive(true);

            accountEntitlement =
                    accountEntitlementRepository.save(
                            accountEntitlement
                    );

            auditEventService.createAuditEvent(
                    "PROVISIONING",
                    "ACCOUNT_ENTITLEMENT",
                    accountEntitlement.getId(),
                    "ENTITLEMENT_REACTIVATED",
                    entitlement.getEntitlementName()
                            + " reactivated for account "
                            + account.getId()
                            + " through access request "
                            + accessRequest.getId(),
                    "SYSTEM"
            );

            return;
        }

        // =====================================================
        // NEW ASSIGNMENT
        // =====================================================

        AccountEntitlement accountEntitlement =
                new AccountEntitlement();

        accountEntitlement.setAccount(account);

        accountEntitlement.setEntitlement(
                entitlement
        );

        accountEntitlement.setActive(true);

        accountEntitlement =
                accountEntitlementRepository.save(
                        accountEntitlement
                );

        auditEventService.createAuditEvent(
                "PROVISIONING",
                "ACCOUNT_ENTITLEMENT",
                accountEntitlement.getId(),
                "ENTITLEMENT_PROVISIONED",
                entitlement.getEntitlementName()
                        + " provisioned to account "
                        + account.getId()
                        + " through access request "
                        + accessRequest.getId(),
                "SYSTEM"
        );
    }
}