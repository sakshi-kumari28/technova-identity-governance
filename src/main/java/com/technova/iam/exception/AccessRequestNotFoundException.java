package com.technova.iam.exception;

public class AccessRequestNotFoundException extends RuntimeException {

    public AccessRequestNotFoundException(String message) {
        super(message);
    }
}