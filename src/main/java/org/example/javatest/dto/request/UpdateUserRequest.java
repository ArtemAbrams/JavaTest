package org.example.javatest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.javatest.validation.CustomEmail;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateUserRequest {

    @NotNull(message = "Id cannot be null")
    private Long id;

    @CustomEmail(message = "Invalid email")
    private String email;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}
