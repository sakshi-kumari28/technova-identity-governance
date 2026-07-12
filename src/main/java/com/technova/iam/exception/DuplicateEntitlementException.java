package com.technova.iam.exception;

public class DuplicateEntitlementException extends RuntimeException {

    public DuplicateEntitlementException(String message) {
        super(message);
    }
}