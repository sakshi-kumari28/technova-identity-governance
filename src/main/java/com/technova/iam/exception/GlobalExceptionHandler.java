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

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFound(
            EmployeeNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Employee Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateEmployeeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmployee(
            DuplicateEmployeeException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Duplicate Employee",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleApplicationNotFound(
            ApplicationNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Application Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateApplication(
            DuplicateApplicationException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Duplicate Application",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccountNotFound(
            AccountNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Account Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAccount(
            DuplicateAccountException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Duplicate Account",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(EntitlementNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntitlementNotFound(
            EntitlementNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Entitlement Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateEntitlementException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntitlement(
            DuplicateEntitlementException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Duplicate Entitlement",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateAccountEntitlementException.class)
    public ResponseEntity<Map<String, String>>
            handleDuplicateAccountEntitlement(
                    DuplicateAccountEntitlementException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Duplicate Account Entitlement",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(AccountEntitlementNotFoundException.class)
    public ResponseEntity<Map<String, String>>
            handleAccountEntitlementNotFound(
                    AccountEntitlementNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Account Entitlement Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ApplicationMismatchException.class)
    public ResponseEntity<Map<String, String>> handleApplicationMismatch(
            ApplicationMismatchException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Application Mismatch",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(AccessRequestNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccessRequestNotFound(
            AccessRequestNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Access Request Not Found",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(InvalidAccessRequestDecisionException.class)
    public ResponseEntity<Map<String, String>>
            handleInvalidAccessRequestDecision(
                    InvalidAccessRequestDecisionException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Invalid Access Request Decision",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(AccessRequestAlreadyReviewedException.class)
    public ResponseEntity<Map<String, String>>
            handleAccessRequestAlreadyReviewed(
                    AccessRequestAlreadyReviewedException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Access Request Already Reviewed",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(DuplicateAccessRequestException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAccessRequest(
            DuplicateAccessRequestException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Duplicate Access Request",
                        "message", exception.getMessage()
                ));
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