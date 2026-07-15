package com.technova.iam.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.technova.iam.dto.CreateLifecycleEventRequest;
import com.technova.iam.dto.MoverLifecycleRequest;
import com.technova.iam.entity.Account;
import com.technova.iam.entity.AccountEntitlement;
import com.technova.iam.entity.Employee;
import com.technova.iam.entity.EmployeeStatus;
import com.technova.iam.entity.LifecycleEvent;
import com.technova.iam.exception.EmployeeNotFoundException;
import com.technova.iam.exception.InvalidLifecycleEventTypeException;
import com.technova.iam.exception.LifecycleEventNotFoundException;
import com.technova.iam.repository.AccountEntitlementRepository;
import com.technova.iam.repository.AccountRepository;
import com.technova.iam.repository.EmployeeRepository;
import com.technova.iam.repository.LifecycleEventRepository;

@Service
public class LifecycleEventService {

    private final LifecycleEventRepository lifecycleEventRepository;
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final AccountEntitlementRepository accountEntitlementRepository;
    private final AuditEventService auditEventService;

    public LifecycleEventService(
            LifecycleEventRepository lifecycleEventRepository,
            EmployeeRepository employeeRepository,
            AccountRepository accountRepository,
            AccountEntitlementRepository accountEntitlementRepository,
            AuditEventService auditEventService) {

        this.lifecycleEventRepository = lifecycleEventRepository;
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.accountEntitlementRepository =
                accountEntitlementRepository;
        this.auditEventService = auditEventService;
    }

    // =========================================================
    // JOINER AND LEAVER WORKFLOW
    // =========================================================

    @Transactional
    public LifecycleEvent createLifecycleEvent(
            CreateLifecycleEventRequest request) {

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

       // Step 2: Normalize event type
String eventType = request
        .getEventType()
        .trim()
        .toUpperCase();

// Step 3: Validate event type
if (!"JOINER".equals(eventType)
        && !"LEAVER".equals(eventType)) {

    throw new InvalidLifecycleEventTypeException(
            "Event type must be JOINER or LEAVER. "
                    + "Use /api/lifecycle-events/mover "
                    + "for MOVER events"
    );
}

// Step 4: Prevent duplicate JOINER
if ("JOINER".equals(eventType)
        && employee.getStatus() == EmployeeStatus.ACTIVE) {

    throw new IllegalStateException(
            "Employee is already ACTIVE."
    );
}

// Step 5: Prevent duplicate LEAVER
if ("LEAVER".equals(eventType)
        && employee.getStatus() == EmployeeStatus.INACTIVE) {

    throw new IllegalStateException(
            "Employee is already INACTIVE."
    );
}

        LifecycleEvent lifecycleEvent =
                new LifecycleEvent();

        lifecycleEvent.setEmployee(employee);
        lifecycleEvent.setEventType(eventType);
        lifecycleEvent.setStatus("IN_PROGRESS");
        lifecycleEvent.setDescription(
                request.getDescription()
        );
        lifecycleEvent.setCreatedAt(
                LocalDateTime.now()
        );

        lifecycleEvent =
                lifecycleEventRepository.save(
                        lifecycleEvent
                );

        if ("JOINER".equals(eventType)) {

            processJoiner(
                    employee,
                    lifecycleEvent
            );
        }

        if ("LEAVER".equals(eventType)) {

            processLeaver(
                    employee,
                    lifecycleEvent
            );
        }

        lifecycleEvent.setStatus("COMPLETED");

        lifecycleEvent.setCompletedAt(
                LocalDateTime.now()
        );

        lifecycleEvent =
                lifecycleEventRepository.save(
                        lifecycleEvent
                );

        // =====================================================
        // AUDIT: LIFECYCLE EVENT COMPLETED
        // =====================================================

        auditEventService.createAuditEvent(
                "LIFECYCLE",
                "LIFECYCLE_EVENT",
                lifecycleEvent.getId(),
                eventType + "_PROCESSED",
                eventType
                        + " lifecycle event completed for employee "
                        + employee.getId(),
                "SYSTEM"
        );

        return lifecycleEvent;
    }

    // =========================================================
    // JOINER PROCESSING
    // =========================================================

    private void processJoiner(
            Employee employee,
            LifecycleEvent lifecycleEvent) {

        EmployeeStatus previousStatus =
                employee.getStatus();

        employee.setStatus(
                EmployeeStatus.ACTIVE
        );

        employeeRepository.save(employee);

        // Log only if status actually changed
        if (previousStatus != EmployeeStatus.ACTIVE) {

            auditEventService.createAuditEvent(
                    "LIFECYCLE",
                    "EMPLOYEE",
                    employee.getId(),
                    "EMPLOYEE_ACTIVATED",
                    "Employee "
                            + employee.getId()
                            + " activated through JOINER event "
                            + lifecycleEvent.getId(),
                    "SYSTEM"
            );
        }
    }

    // =========================================================
    // MOVER WORKFLOW
    // =========================================================

    @Transactional
    public LifecycleEvent processMoverLifecycle(
            MoverLifecycleRequest request) {

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

        String oldDepartment =
                employee.getDepartment();

        String oldJobTitle =
                employee.getJobTitle();

        LifecycleEvent lifecycleEvent =
                new LifecycleEvent();

        lifecycleEvent.setEmployee(employee);

        lifecycleEvent.setEventType("MOVER");

        lifecycleEvent.setStatus("IN_PROGRESS");

        lifecycleEvent.setDescription(
                request.getDescription()
                        + " | Department: "
                        + oldDepartment
                        + " -> "
                        + request.getNewDepartment()
                        + " | Job Title: "
                        + oldJobTitle
                        + " -> "
                        + request.getNewJobTitle()
        );

        lifecycleEvent.setCreatedAt(
                LocalDateTime.now()
        );

        lifecycleEvent =
                lifecycleEventRepository.save(
                        lifecycleEvent
                );

        employee.setDepartment(
                request.getNewDepartment()
        );

        employee.setJobTitle(
                request.getNewJobTitle()
        );

        employeeRepository.save(employee);

        lifecycleEvent.setStatus("COMPLETED");

        lifecycleEvent.setCompletedAt(
                LocalDateTime.now()
        );

        lifecycleEvent =
                lifecycleEventRepository.save(
                        lifecycleEvent
                );

        // =====================================================
        // AUDIT: EMPLOYEE UPDATED
        // =====================================================

        auditEventService.createAuditEvent(
                "LIFECYCLE",
                "EMPLOYEE",
                employee.getId(),
                "EMPLOYEE_MOVED",
                "Employee "
                        + employee.getId()
                        + " moved from department "
                        + oldDepartment
                        + " to "
                        + request.getNewDepartment()
                        + " and job title "
                        + oldJobTitle
                        + " to "
                        + request.getNewJobTitle(),
                "SYSTEM"
        );

        // =====================================================
        // AUDIT: MOVER COMPLETED
        // =====================================================

        auditEventService.createAuditEvent(
                "LIFECYCLE",
                "LIFECYCLE_EVENT",
                lifecycleEvent.getId(),
                "MOVER_PROCESSED",
                "MOVER lifecycle event completed for employee "
                        + employee.getId(),
                "SYSTEM"
        );

        return lifecycleEvent;
    }

    // =========================================================
    // LEAVER PROCESSING
    // =========================================================

    private void processLeaver(
            Employee employee,
            LifecycleEvent lifecycleEvent) {

        // =====================================================
        // DEACTIVATE EMPLOYEE
        // =====================================================

        if (employee.getStatus() != EmployeeStatus.INACTIVE) {

            employee.setStatus(
                    EmployeeStatus.INACTIVE
            );

            employeeRepository.save(employee);

            auditEventService.createAuditEvent(
                    "LIFECYCLE",
                    "EMPLOYEE",
                    employee.getId(),
                    "EMPLOYEE_DEACTIVATED",
                    "Employee "
                            + employee.getId()
                            + " deactivated through LEAVER event "
                            + lifecycleEvent.getId(),
                    "SYSTEM"
            );
        }

        List<Account> accounts =
                accountRepository.findByEmployeeId(
                        employee.getId()
                );

        for (Account account : accounts) {

            // =================================================
            // DEACTIVATE ACCOUNT
            // =================================================

            if (account.isActive()) {

                account.setActive(false);

                account =
                        accountRepository.save(account);

                auditEventService.createAuditEvent(
                        "LIFECYCLE",
                        "ACCOUNT",
                        account.getId(),
                        "ACCOUNT_DEACTIVATED",
                        "Account "
                                + account.getId()
                                + " deactivated through LEAVER event "
                                + lifecycleEvent.getId(),
                        "SYSTEM"
                );
            }

            List<AccountEntitlement> assignments =
                    accountEntitlementRepository
                            .findByAccountId(
                                    account.getId()
                            );

            for (AccountEntitlement assignment
                    : assignments) {

                // Log only actual state changes
                if (assignment.isActive()) {

                    assignment.setActive(false);

                    assignment =
                            accountEntitlementRepository.save(
                                    assignment
                            );

                    auditEventService.createAuditEvent(
                            "PROVISIONING",
                            "ACCOUNT_ENTITLEMENT",
                            assignment.getId(),
                            "ENTITLEMENT_DEPROVISIONED",
                            assignment
                                    .getEntitlement()
                                    .getEntitlementName()
                                    + " deprovisioned from account "
                                    + account.getId()
                                    + " through LEAVER event "
                                    + lifecycleEvent.getId(),
                            "SYSTEM"
                    );
                }
            }
        }
    }

    // =========================================================
    // GET ALL LIFECYCLE EVENTS
    // =========================================================

    public List<LifecycleEvent> getAllLifecycleEvents() {

        return lifecycleEventRepository.findAll();
    }

    // =========================================================
    // GET LIFECYCLE EVENT BY ID
    // =========================================================

    public LifecycleEvent getLifecycleEventById(
            Long id) {

        return lifecycleEventRepository
                .findById(id)
                .orElseThrow(() ->
                        new LifecycleEventNotFoundException(
                                "Lifecycle event not found with id: "
                                        + id
                        )
                );
    }
}