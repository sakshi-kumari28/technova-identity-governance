package com.technova.iam.exception;

public class DuplicateRoleEntitlementException extends RuntimeException {

    public DuplicateRoleEntitlementException(String message) {
        super(message);
    }
}