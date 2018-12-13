package com.chulm.apns.exception;

public class SocketNotCreatedException extends RuntimeException {
    public SocketNotCreatedException() {
        super();
    }

    public SocketNotCreatedException(String message) {
        super(message);
    }

    public SocketNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketNotCreatedException(Throwable cause) {
        super(cause);
    }
}