package com.technova.iam.exception;

public class DuplicateAccountEntitlementException extends RuntimeException {

    public DuplicateAccountEntitlementException(String message) {
        super(message);
    }
}