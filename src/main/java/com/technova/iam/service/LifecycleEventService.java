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

    public LifecycleEventService(
            LifecycleEventRepository lifecycleEventRepository,
            EmployeeRepository employeeRepository,
            AccountRepository accountRepository,
            AccountEntitlementRepository accountEntitlementRepository) {

        this.lifecycleEventRepository = lifecycleEventRepository;
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.accountEntitlementRepository = accountEntitlementRepository;
    }

    // =========================================================
    // JOINER AND LEAVER WORKFLOW
    // =========================================================

    @Transactional
    public LifecycleEvent createLifecycleEvent(
            CreateLifecycleEventRequest request) {

        // Step 1: Find employee
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

        // Step 3: Only JOINER and LEAVER are allowed here
        if (!"JOINER".equals(eventType)
                && !"LEAVER".equals(eventType)) {

            throw new InvalidLifecycleEventTypeException(
                    "Event type must be JOINER or LEAVER. "
                            + "Use /api/lifecycle-events/mover "
                            + "for MOVER events"
            );
        }

        // Step 4: Create lifecycle event
        LifecycleEvent lifecycleEvent = new LifecycleEvent();

        lifecycleEvent.setEmployee(employee);
        lifecycleEvent.setEventType(eventType);
        lifecycleEvent.setStatus("IN_PROGRESS");
        lifecycleEvent.setDescription(request.getDescription());
        lifecycleEvent.setCreatedAt(LocalDateTime.now());

        // Step 5: Save initial event
        lifecycleEvent =
                lifecycleEventRepository.save(lifecycleEvent);

        // Step 6: Process JOINER
        if ("JOINER".equals(eventType)) {

            processJoiner(employee);
        }

        // Step 7: Process LEAVER
        if ("LEAVER".equals(eventType)) {

            processLeaver(employee);
        }

        // Step 8: Complete event
        lifecycleEvent.setStatus("COMPLETED");
        lifecycleEvent.setCompletedAt(LocalDateTime.now());

        // Step 9: Save completed event
        return lifecycleEventRepository.save(lifecycleEvent);
    }

    // =========================================================
    // JOINER PROCESSING
    // =========================================================

    private void processJoiner(Employee employee) {

        employee.setStatus(EmployeeStatus.ACTIVE);

        employeeRepository.save(employee);
    }

    // =========================================================
    // MOVER WORKFLOW
    // =========================================================

    @Transactional
    public LifecycleEvent processMoverLifecycle(
            MoverLifecycleRequest request) {

        // Step 1: Find employee
        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

        // Step 2: Store previous employee information
        String oldDepartment =
                employee.getDepartment();

        String oldJobTitle =
                employee.getJobTitle();

        // Step 3: Create lifecycle event
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

        // Step 4: Save initial lifecycle event
        lifecycleEvent =
                lifecycleEventRepository.save(
                        lifecycleEvent
                );

        // Step 5: Update employee department
        employee.setDepartment(
                request.getNewDepartment()
        );

        // Step 6: Update employee job title
        employee.setJobTitle(
                request.getNewJobTitle()
        );

        // Step 7: Save updated employee
        employeeRepository.save(employee);

        // Step 8: Complete lifecycle event
        lifecycleEvent.setStatus("COMPLETED");

        lifecycleEvent.setCompletedAt(
                LocalDateTime.now()
        );

        // Step 9: Save completed lifecycle event
        return lifecycleEventRepository.save(
                lifecycleEvent
        );
    }

    // =========================================================
    // LEAVER PROCESSING
    // =========================================================

    private void processLeaver(Employee employee) {

        // Step 1: Deactivate employee
        employee.setStatus(
                EmployeeStatus.INACTIVE
        );

        employeeRepository.save(employee);

        // Step 2: Find all accounts owned by employee
        List<Account> accounts =
                accountRepository.findByEmployeeId(
                        employee.getId()
                );

        // Step 3: Process every employee account
        for (Account account : accounts) {

            // Deactivate account
            account.setActive(false);

            accountRepository.save(account);

            // Step 4: Find entitlement assignments
            List<AccountEntitlement> assignments =
                    accountEntitlementRepository
                            .findByAccountId(
                                    account.getId()
                            );

            // Step 5: Deactivate every assignment
            for (AccountEntitlement assignment : assignments) {

                assignment.setActive(false);

                accountEntitlementRepository.save(
                        assignment
                );
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