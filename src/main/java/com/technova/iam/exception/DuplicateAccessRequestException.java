package com.technova.iam.exception;

public class DuplicateAccessRequestException extends RuntimeException {

    public DuplicateAccessRequestException(String message) {
        super(message);
    }
}