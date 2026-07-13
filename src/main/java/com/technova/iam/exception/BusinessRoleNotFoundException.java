package com.technova.iam.exception;

public class BusinessRoleNotFoundException extends RuntimeException {

    public BusinessRoleNotFoundException(String message) {
        super(message);
    }
}