package org.example.javatest.exhandler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.jbosslog.JBossLog;
import org.apache.coyote.BadRequestException;
import org.example.javatest.dto.error.ApiError;
import org.example.javatest.exception.AgeLessEighteenException;
import org.example.javatest.exception.InvalidDateRangeException;
import org.example.javatest.exception.UserExistException;
import org.example.javatest.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@JBossLog
@ControllerAdvice
public class GlobalExceptionHandler extends GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unhandled exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.INTERNAL_SERVER_ERROR, this.errorBuilder(HttpStatus.INTERNAL_SERVER_ERROR,
                "Sorry, we are unable to process your request right now. Please try again later", request).build());
    }

    @ExceptionHandler(AgeLessEighteenException.class)
    public ResponseEntity<ApiError> handleAgeLessEighteenException(AgeLessEighteenException ex, WebRequest request) {
        log.error("Age less eighteen exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.BAD_REQUEST, this.errorBuilder(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request).build());
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.error("Constraint exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.BAD_REQUEST, this.errorBuilder(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request).build());
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ApiError> handleUserExistException(UserExistException ex, WebRequest request) {
        log.error("User exist exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.BAD_REQUEST, this.errorBuilder(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request).build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.error("User not found exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.NOT_FOUND, this.errorBuilder(HttpStatus.NOT_FOUND,
                ex.getMessage(), request).build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.error("Bad request exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.BAD_REQUEST, this.errorBuilder(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Method argument not valid exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.BAD_REQUEST, this.errorBuilder(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request).build());
    }
    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiError> handleInvalidDateRangeException(InvalidDateRangeException ex, WebRequest request) {
        log.error("Invalid date range exception exception caught: " + ex.getMessage() + "; Path: " + request.getDescription(false), ex);
        return this.buildRestResponse(HttpStatus.BAD_REQUEST, this.errorBuilder(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request).build());
    }
}
