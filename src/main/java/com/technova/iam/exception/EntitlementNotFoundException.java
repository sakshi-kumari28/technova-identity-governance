package com.technova.iam.exception;

public class EntitlementNotFoundException extends RuntimeException {

    public EntitlementNotFoundException(String message) {
        super(message);
    }
}