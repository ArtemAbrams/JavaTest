package org.example.javatest;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.javatest.dto.request.CreateUserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateUserRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCreateUserRequest() {
        var request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.of(1990, 5, 15));

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        var request = new CreateUserRequest();
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
        var request = new CreateUserRequest();
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
        var request = new CreateUserRequest();
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
        var request = new CreateUserRequest();
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
