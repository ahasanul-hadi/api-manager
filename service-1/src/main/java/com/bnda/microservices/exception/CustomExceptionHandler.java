package com.bnda.microservices.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleControllerException(final ApplicationException exception, HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        log.error(String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()), exception);
        var response = new ApiErrorResponse(
                guid,
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response,  exception.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(final Exception exception, final HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        HttpStatus status = getStatus(request);
        log.error(String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()), exception);
        var response = new ApiErrorResponse(
                guid,
                "Internal server error",
                status.value(),
                status.name(),
                request.getRequestURI(),
                request.getMethod(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus status = HttpStatus.resolve(code);
        return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }


}
