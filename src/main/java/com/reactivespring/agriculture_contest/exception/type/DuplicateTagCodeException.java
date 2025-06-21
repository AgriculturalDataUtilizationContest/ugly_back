package com.reactivespring.agriculture_contest.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateTagCodeException extends RuntimeException {
    public DuplicateTagCodeException(String message) {
        super(message);
    }

    public DuplicateTagCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
