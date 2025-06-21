package com.reactivespring.agriculture_contest.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UploadNotificationException extends RuntimeException {
    public UploadNotificationException(String message) {
        super(message);
    }

    public UploadNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
