package org.example.javatest.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<CustomEmail, String> {

    private static final String EMAIL_PATTERN =
            "^[\\w.-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && Pattern.matches(EMAIL_PATTERN, email);
    }
}
