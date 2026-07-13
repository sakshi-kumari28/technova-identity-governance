package com.technova.iam.exception;

public class DuplicateBusinessRoleException extends RuntimeException {

    public DuplicateBusinessRoleException(String message) {
        super(message);
    }
}