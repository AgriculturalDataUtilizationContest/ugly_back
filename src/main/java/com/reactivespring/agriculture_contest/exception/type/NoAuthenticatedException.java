package com.reactivespring.agriculture_contest.exception.type;

package com.reactivespring.agriculture_contest.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoAuthenticatedException extends RuntimeException {
    public NoAuthenticatedException(String message) {
        super(message);
    }

    public NoAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
