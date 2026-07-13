package com.technova.iam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.technova.iam.dto.AssignBusinessRoleRequest;
import com.technova.iam.entity.Account;
import com.technova.iam.entity.AccountEntitlement;
import com.technova.iam.entity.BusinessRole;
import com.technova.iam.entity.Employee;
import com.technova.iam.entity.EmployeeBusinessRole;
import com.technova.iam.entity.RoleEntitlement;
import com.technova.iam.exception.BusinessRoleNotFoundException;
import com.technova.iam.exception.DuplicateEmployeeBusinessRoleException;
import com.technova.iam.exception.EmployeeBusinessRoleNotFoundException;
import com.technova.iam.exception.EmployeeNotFoundException;
import com.technova.iam.repository.AccountEntitlementRepository;
import com.technova.iam.repository.AccountRepository;
import com.technova.iam.repository.BusinessRoleRepository;
import com.technova.iam.repository.EmployeeBusinessRoleRepository;
import com.technova.iam.repository.EmployeeRepository;
import com.technova.iam.repository.RoleEntitlementRepository;

@Service
public class EmployeeBusinessRoleService {

    private final EmployeeBusinessRoleRepository employeeBusinessRoleRepository;
    private final EmployeeRepository employeeRepository;
    private final BusinessRoleRepository businessRoleRepository;
    private final RoleEntitlementRepository roleEntitlementRepository;
    private final AccountRepository accountRepository;
    private final AccountEntitlementRepository accountEntitlementRepository;

    public EmployeeBusinessRoleService(
            EmployeeBusinessRoleRepository employeeBusinessRoleRepository,
            EmployeeRepository employeeRepository,
            BusinessRoleRepository businessRoleRepository,
            RoleEntitlementRepository roleEntitlementRepository,
            AccountRepository accountRepository,
            AccountEntitlementRepository accountEntitlementRepository) {

        this.employeeBusinessRoleRepository = employeeBusinessRoleRepository;
        this.employeeRepository = employeeRepository;
        this.businessRoleRepository = businessRoleRepository;
        this.roleEntitlementRepository = roleEntitlementRepository;
        this.accountRepository = accountRepository;
        this.accountEntitlementRepository = accountEntitlementRepository;
    }

    // =========================================================
    // ASSIGN BUSINESS ROLE
    // =========================================================

    @Transactional
    public EmployeeBusinessRole assignBusinessRole(
            AssignBusinessRoleRequest request) {

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

        BusinessRole businessRole = businessRoleRepository
                .findById(request.getBusinessRoleId())
                .orElseThrow(() ->
                        new BusinessRoleNotFoundException(
                                "Business role not found with id: "
                                        + request.getBusinessRoleId()
                        )
                );

        if (!businessRole.isActive()) {

            throw new IllegalStateException(
                    "Cannot assign inactive business role: "
                            + businessRole.getRoleName()
            );
        }

        Optional<EmployeeBusinessRole> existingAssignment =
                employeeBusinessRoleRepository
                        .findByEmployeeIdAndBusinessRoleId(
                                employee.getId(),
                                businessRole.getId()
                        );

        EmployeeBusinessRole employeeBusinessRole;

        if (existingAssignment.isPresent()) {

            employeeBusinessRole = existingAssignment.get();

            if (employeeBusinessRole.isActive()) {

                throw new DuplicateEmployeeBusinessRoleException(
                        "Employee "
                                + employee.getId()
                                + " already has business role "
                                + businessRole.getId()
                );
            }

            employeeBusinessRole.setActive(true);
            employeeBusinessRole.setAssignedAt(LocalDateTime.now());
            employeeBusinessRole.setRevokedAt(null);

        } else {

            employeeBusinessRole = new EmployeeBusinessRole();

            employeeBusinessRole.setEmployee(employee);
            employeeBusinessRole.setBusinessRole(businessRole);
            employeeBusinessRole.setActive(true);
            employeeBusinessRole.setAssignedAt(LocalDateTime.now());
            employeeBusinessRole.setRevokedAt(null);
        }

        employeeBusinessRole =
                employeeBusinessRoleRepository.save(
                        employeeBusinessRole
                );

        provisionRoleEntitlements(
                employee,
                businessRole
        );

        return employeeBusinessRole;
    }

    // =========================================================
    // AUTOMATIC RBAC PROVISIONING
    // =========================================================

    private void provisionRoleEntitlements(
            Employee employee,
            BusinessRole businessRole) {

        List<RoleEntitlement> roleEntitlements =
                roleEntitlementRepository
                        .findByBusinessRoleIdAndActiveTrue(
                                businessRole.getId()
                        );

        for (RoleEntitlement roleEntitlement : roleEntitlements) {

            Long applicationId =
                    roleEntitlement
                            .getEntitlement()
                            .getTargetApplication()
                            .getId();

            Optional<Account> accountOptional =
                    accountRepository
                            .findByEmployeeIdAndTargetApplicationId(
                                    employee.getId(),
                                    applicationId
                            );

            if (accountOptional.isEmpty()) {
                continue;
            }

            Account account = accountOptional.get();

            if (!account.isActive()) {
                continue;
            }

            Long entitlementId =
                    roleEntitlement
                            .getEntitlement()
                            .getId();

            Optional<AccountEntitlement> existingAccountEntitlement =
                    accountEntitlementRepository
                            .findByAccountIdAndEntitlementId(
                                    account.getId(),
                                    entitlementId
                            );

            if (existingAccountEntitlement.isPresent()) {

                AccountEntitlement accountEntitlement =
                        existingAccountEntitlement.get();

                if (!accountEntitlement.isActive()) {

                    accountEntitlement.setActive(true);

                    accountEntitlementRepository.save(
                            accountEntitlement
                    );
                }

                continue;
            }

            AccountEntitlement accountEntitlement =
                    new AccountEntitlement();

            accountEntitlement.setAccount(account);

            accountEntitlement.setEntitlement(
                    roleEntitlement.getEntitlement()
            );

            accountEntitlement.setActive(true);

            accountEntitlementRepository.save(
                    accountEntitlement
            );
        }
    }

    // =========================================================
    // GET ALL ASSIGNMENTS
    // =========================================================

    public List<EmployeeBusinessRole> getAllEmployeeBusinessRoles() {

        return employeeBusinessRoleRepository.findAll();
    }

    // =========================================================
    // GET ASSIGNMENT BY ID
    // =========================================================

    public EmployeeBusinessRole getEmployeeBusinessRoleById(
            Long id) {

        return employeeBusinessRoleRepository
                .findById(id)
                .orElseThrow(() ->
                        new EmployeeBusinessRoleNotFoundException(
                                "Employee business role assignment "
                                        + "not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // GET ASSIGNMENTS BY EMPLOYEE
    // =========================================================

    public List<EmployeeBusinessRole> getEmployeeBusinessRolesByEmployee(
            Long employeeId) {

        if (!employeeRepository.existsById(employeeId)) {

            throw new EmployeeNotFoundException(
                    employeeId
            );
        }

        return employeeBusinessRoleRepository
                .findByEmployeeId(employeeId);
    }

    // =========================================================
    // REVOKE BUSINESS ROLE
    // =========================================================

    @Transactional
    public EmployeeBusinessRole revokeBusinessRole(
            Long id) {

        EmployeeBusinessRole employeeBusinessRole =
                getEmployeeBusinessRoleById(id);

        if (!employeeBusinessRole.isActive()) {

            throw new IllegalStateException(
                    "Employee business role assignment "
                            + "is already revoked"
            );
        }

        Employee employee =
                employeeBusinessRole.getEmployee();

        BusinessRole businessRole =
                employeeBusinessRole.getBusinessRole();

        employeeBusinessRole.setActive(false);

        employeeBusinessRole.setRevokedAt(
                LocalDateTime.now()
        );

        employeeBusinessRole =
                employeeBusinessRoleRepository.save(
                        employeeBusinessRole
                );

        deprovisionRoleEntitlements(
                employee,
                businessRole
        );

        return employeeBusinessRole;
    }

    // =========================================================
    // SAFE RBAC DEPROVISIONING
    // =========================================================

    private void deprovisionRoleEntitlements(
            Employee employee,
            BusinessRole revokedBusinessRole) {

        List<RoleEntitlement> revokedRoleEntitlements =
                roleEntitlementRepository
                        .findByBusinessRoleIdAndActiveTrue(
                                revokedBusinessRole.getId()
                        );

        List<EmployeeBusinessRole> remainingActiveRoles =
                employeeBusinessRoleRepository
                        .findByEmployeeIdAndActiveTrue(
                                employee.getId()
                        );

        for (RoleEntitlement revokedRoleEntitlement
                : revokedRoleEntitlements) {

            Long entitlementId =
                    revokedRoleEntitlement
                            .getEntitlement()
                            .getId();

            boolean grantedByAnotherRole = false;

            for (EmployeeBusinessRole activeRoleAssignment
                    : remainingActiveRoles) {

                Long activeBusinessRoleId =
                        activeRoleAssignment
                                .getBusinessRole()
                                .getId();

                boolean roleStillGrantsEntitlement =
                        roleEntitlementRepository
                                .existsByBusinessRoleIdAndEntitlementIdAndActiveTrue(
                                        activeBusinessRoleId,
                                        entitlementId
                                );

                if (roleStillGrantsEntitlement) {

                    grantedByAnotherRole = true;

                    break;
                }
            }

            if (grantedByAnotherRole) {
                continue;
            }

            Long applicationId =
                    revokedRoleEntitlement
                            .getEntitlement()
                            .getTargetApplication()
                            .getId();

            Optional<Account> accountOptional =
                    accountRepository
                            .findByEmployeeIdAndTargetApplicationId(
                                    employee.getId(),
                                    applicationId
                            );

            if (accountOptional.isEmpty()) {
                continue;
            }

            Account account = accountOptional.get();

            Optional<AccountEntitlement> accountEntitlementOptional =
                    accountEntitlementRepository
                            .findByAccountIdAndEntitlementId(
                                    account.getId(),
                                    entitlementId
                            );

            if (accountEntitlementOptional.isEmpty()) {
                continue;
            }

            AccountEntitlement accountEntitlement =
                    accountEntitlementOptional.get();

            if (accountEntitlement.isActive()) {

                accountEntitlement.setActive(false);

                accountEntitlementRepository.save(
                        accountEntitlement
                );
            }
        }
    }
}