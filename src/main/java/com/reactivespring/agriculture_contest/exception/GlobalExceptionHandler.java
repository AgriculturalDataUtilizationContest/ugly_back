package com.reactivespring.agriculture_contest.exception;

import com.reactivespring.agriculture_contest.exception.type.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidTokenException.class,
            NoAuthenticatedException.class,
            NoAuthorizationException.class,
            DuplicateEntityException.class,
            DuplicateTagCodeException.class,
            NotFoundException.class,
            DataUpdateException.class,
            UploadNotificationException.class,
            PresignedUrlCreationException.class,
            IllegalArgumentException.class,
            AlreadyExistException.class,
            UnauthorizedAccessException.class,

            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class,
            ConstraintViolationException.class,
            AccessDeniedException.class,
            TypeMismatchException.class,
            BindException.class,
            ConversionFailedException.class,
            MaxUploadSizeExceededException.class
    })
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(Exception ex) {
        HttpStatus status = resolveHttpStatus(ex);

        String message = extractMessage(ex);

        ExceptionResponse body = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllUncaughtException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ExceptionResponse body = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message("Internal Server Error")
                .build();

        return ResponseEntity.status(status).body(body);
    }

    private HttpStatus resolveHttpStatus(Exception ex) {
        ResponseStatus annotation = ex.getClass().getAnnotation(ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        }

        if (ex instanceof IllegalArgumentException
                || ex instanceof MethodArgumentNotValidException
                || ex instanceof HttpMessageNotReadableException
                || ex instanceof MissingServletRequestParameterException
                || ex instanceof ConstraintViolationException
                || ex instanceof BindException
                || ex instanceof ConversionFailedException) {
            return HttpStatus.BAD_REQUEST;
        }

        if (ex instanceof NoAuthenticatedException) {
            return HttpStatus.FORBIDDEN;
        }

        if (ex instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }

        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return HttpStatus.METHOD_NOT_ALLOWED;
        }

        if (ex instanceof MaxUploadSizeExceededException) {
            return HttpStatus.PAYLOAD_TOO_LARGE;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String extractMessage(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            // 유효성 검증 실패 메시지 간략화
            return ((MethodArgumentNotValidException) ex).getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .findFirst()
                    .orElse(ex.getMessage());
        }

        if (ex instanceof ConstraintViolationException) {
            return ((ConstraintViolationException) ex).getConstraintViolations()
                    .stream()
                    .map(cv -> cv.getMessage())
                    .findFirst()
                    .orElse(ex.getMessage());
        }

        if (ex instanceof BindException) {
            return ((BindException) ex).getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .findFirst()
                    .orElse(ex.getMessage());
        }

        return ex.getMessage() != null ? ex.getMessage() : "Unexpected error";
    }

    @Getter
    @Builder
    public static class ExceptionResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
    }
}
