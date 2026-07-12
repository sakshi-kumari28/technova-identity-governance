package com.technova.iam.exception;

public class InvalidLifecycleEventTypeException extends RuntimeException {

    public InvalidLifecycleEventTypeException(String message) {
        super(message);
    }
}