package com.pawelnu.BackendProjectManager.exception;

public class NotNullOrEmptyException extends RuntimeException {

    public  NotNullOrEmptyException(String message) {
        super(message);
    }
}
