package com.reactivespring.agriculture_contest.exception.type;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
