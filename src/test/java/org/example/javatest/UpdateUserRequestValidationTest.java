package org.example.javatest;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.javatest.dto.request.UpdateUserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateUserRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUpdateUserRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.of(1990, 5, 15));

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullId() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.of(1990, 5, 15));

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Id cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        request.setEmail("invalidemail");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.of(1990, 5, 15));

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankFirstName() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        request.setEmail("test@example.com");
        request.setFirstName("");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.of(1990, 5, 15));

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("First name cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankLastName() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("");
        request.setBirthDate(LocalDate.of(1990, 5, 15));

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Last name cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testFutureBirthDate() {
        var request = new UpdateUserRequest();
        request.setId(1L);
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.now().plusDays(1));

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Birth date must be in the past", violations.iterator().next().getMessage());
    }
}
