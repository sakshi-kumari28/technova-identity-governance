package com.technova.iam.exception;

public class DuplicateEmployeeBusinessRoleException extends RuntimeException {

    public DuplicateEmployeeBusinessRoleException(String message) {
        super(message);
    }
}