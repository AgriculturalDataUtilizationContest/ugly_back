package com.reactivespring.agriculture_contest.exception.type;

public class NoAuthenticatedException extends RuntimeException {
    public NoAuthenticatedException(String message) {
        super(message);
    }

    public NoAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
