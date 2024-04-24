package org.example.javatest;

import org.example.javatest.dto.error.ApiError;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiErrorTest {

    private final Instant now = Instant.now();
    private final ApiError apiError = new ApiError(404, "Not Found", "Resource not found", now, "/some/path");

    @Test
    void testStatusGetter() {
        assertEquals(404, apiError.getStatus(), "The status should be 404");
    }

    @Test
    void testErrorGetter() {
        assertEquals("Not Found", apiError.getError(), "The error message should match 'Not Found'");
    }

    @Test
    void testMessageGetter() {
        assertEquals("Resource not found", apiError.getMessage(), "The message should match 'Resource not found'");
    }

    @Test
    void testTimestampGetter() {
        assertEquals(now, apiError.getTimestamp(), "The timestamp should match the current time");
    }

    @Test
    void testPathGetter() {
        assertEquals("/some/path", apiError.getPath(), "The path should match '/some/path'");
    }

    @Test
    void testEqualityOfApiError() {
        ApiError anotherApiError = new ApiError(404, "Not Found", "Resource not found", now, "/some/path");
        assertEquals(apiError, anotherApiError, "Two ApiError objects with the same properties should be equal");
    }

    @Test
    void testHashCodeOfApiError() {
        ApiError anotherApiError = new ApiError(404, "Not Found", "Resource not found", now, "/some/path");
        assertEquals(apiError.hashCode(), anotherApiError.hashCode(), "Hash codes of two identical ApiError objects should be the same");
    }
}
