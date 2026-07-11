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
  @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException exception) {

    Map<String, String> errors = new HashMap<>();

    exception.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
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
}