package com.technova.iam.exception;

public class DuplicateEmployeeException extends RuntimeException {

    public DuplicateEmployeeException(String message) {
        super(message);
    }
}