package com.chulm.apns.exception;

public class TokenVerifyException extends RuntimeException {
    public TokenVerifyException() {
        super();
    }

    public TokenVerifyException(String message) {
        super(message);
    }

    public TokenVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenVerifyException(Throwable cause) {
        super(cause);
    }
}