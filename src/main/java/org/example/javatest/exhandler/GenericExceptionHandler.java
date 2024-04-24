package org.example.javatest.exhandler;


import org.example.javatest.dto.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

public abstract class GenericExceptionHandler {

    protected GenericExceptionHandler() {
    }

    protected ResponseEntity<ApiError> buildRestResponse(HttpStatus status, ApiError apiError) {
        return ResponseEntity.status(status).body(apiError);
    }

    protected ApiError.ApiErrorBuilder errorBuilder(HttpStatus status, String message, WebRequest request) {
        return ApiError.builder().error(status.getReasonPhrase()).message(message)
                .path(request.getDescription(false))
                .timestamp(Instant.now()).status(status.value());
    }
}
