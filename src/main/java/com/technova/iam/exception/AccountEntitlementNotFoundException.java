package com.technova.iam.exception;

public class AccountEntitlementNotFoundException extends RuntimeException {

    public AccountEntitlementNotFoundException(String message) {
        super(message);
    }
}