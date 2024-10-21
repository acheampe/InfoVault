package com.infovault.exception;

public class CognitoRegistrationException extends RuntimeException {
    public CognitoRegistrationException(String message) {
        super(message);
    }

    public CognitoRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
