package com.reactivespring.agriculture_contest.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PresignedUrlCreationException extends RuntimeException {
    public PresignedUrlCreationException(String message) {
        super(message);
    }

    public PresignedUrlCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
