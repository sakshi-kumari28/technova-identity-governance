package com.technova.iam.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(AccountEntitlementNotFoundException.class)
public ResponseEntity<Map<String, String>> handleAccountEntitlementNotFound(
        AccountEntitlementNotFoundException exception) {

    Map<String, String> errorResponse = Map.of(
            "error", "Account Entitlement Not Found",
            "message", exception.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
}
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFound(
            EmployeeNotFoundException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Employee Not Found",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(DuplicateEmployeeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmployee(
            DuplicateEmployeeException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Duplicate Employee",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleApplicationNotFound(
            ApplicationNotFoundException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Application Not Found",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateApplication(
            DuplicateApplicationException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Duplicate Application",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccountNotFound(
            AccountNotFoundException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Account Not Found",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAccount(
            DuplicateAccountException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Duplicate Account",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(EntitlementNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntitlementNotFound(
            EntitlementNotFoundException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Entitlement Not Found",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(DuplicateEntitlementException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntitlement(
            DuplicateEntitlementException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Duplicate Entitlement",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(DuplicateAccountEntitlementException.class)
    public ResponseEntity<Map<String, String>>
            handleDuplicateAccountEntitlement(
                    DuplicateAccountEntitlementException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Duplicate Account Entitlement",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(ApplicationMismatchException.class)
    public ResponseEntity<Map<String, String>> handleApplicationMismatch(
            ApplicationMismatchException exception) {

        Map<String, String> errorResponse = Map.of(
                "error", "Application Mismatch",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}