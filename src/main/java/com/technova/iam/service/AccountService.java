package com.technova.iam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technova.iam.dto.CreateAccountRequest;
import com.technova.iam.dto.UpdateAccountRequest;
import com.technova.iam.entity.Account;
import com.technova.iam.entity.Employee;
import com.technova.iam.entity.TargetApplication;
import com.technova.iam.exception.AccountNotFoundException;
import com.technova.iam.exception.ApplicationNotFoundException;
import com.technova.iam.exception.DuplicateAccountException;
import com.technova.iam.exception.EmployeeNotFoundException;
import com.technova.iam.repository.AccountRepository;
import com.technova.iam.repository.EmployeeRepository;
import com.technova.iam.repository.TargetApplicationRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final TargetApplicationRepository targetApplicationRepository;

    public AccountService(
            AccountRepository accountRepository,
            EmployeeRepository employeeRepository,
            TargetApplicationRepository targetApplicationRepository) {

        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
        this.targetApplicationRepository = targetApplicationRepository;
    }

    public Account createAccount(CreateAccountRequest request) {

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

        TargetApplication application = targetApplicationRepository
                .findById(request.getApplicationId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found with id: "
                                        + request.getApplicationId()
                        )
                );

        if (accountRepository.existsByAccountNameAndTargetApplicationId(
                request.getAccountName(),
                request.getApplicationId())) {

            throw new DuplicateAccountException(
                    "Account already exists in this application: "
                            + request.getAccountName()
            );
        }

        Account account = new Account();

        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        account.setActive(true);
        account.setEmployee(employee);
        account.setTargetApplication(application);

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {

        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: " + id
                        )
                );
    }

    public Account updateAccount(
            Long id,
            UpdateAccountRequest request) {

        Account account = getAccountById(id);

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                request.getEmployeeId()
                        )
                );

        TargetApplication application = targetApplicationRepository
                .findById(request.getApplicationId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found with id: "
                                        + request.getApplicationId()
                        )
                );

        if (accountRepository
                .existsByAccountNameAndTargetApplicationIdAndIdNot(
                        request.getAccountName(),
                        request.getApplicationId(),
                        id)) {

            throw new DuplicateAccountException(
                    "Account already exists in this application: "
                            + request.getAccountName()
            );
        }

        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        account.setActive(request.isActive());
        account.setEmployee(employee);
        account.setTargetApplication(application);

        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {

        Account account = getAccountById(id);

        accountRepository.delete(account);
    }
}