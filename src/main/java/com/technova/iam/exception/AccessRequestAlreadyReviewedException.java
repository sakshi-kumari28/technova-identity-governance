package com.technova.iam.exception;

public class AccessRequestAlreadyReviewedException extends RuntimeException {

    public AccessRequestAlreadyReviewedException(String message) {
        super(message);
    }
}