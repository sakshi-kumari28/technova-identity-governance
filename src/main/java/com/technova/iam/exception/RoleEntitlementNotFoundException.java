package com.technova.iam.exception;

public class RoleEntitlementNotFoundException extends RuntimeException {

    public RoleEntitlementNotFoundException(String message) {
        super(message);
    }
}