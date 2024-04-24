package org.example.javatest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.example.javatest.dto.request.CreateUserRequest;
import org.example.javatest.dto.request.UpdateUserRequest;
import org.example.javatest.dto.response.AllUserByDateRangeResponse;
import org.example.javatest.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@JBossLog
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("Creating a new user with details: " + createUserRequest);
        userService.createUser(createUserRequest);
        log.info("User created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        log.info("Updating user with details: " + updateUserRequest);
        userService.updateUser(updateUserRequest);
        log.info("User updated successfully.");

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        log.info("Attempting to delete user with ID: " + id);
        userService.deleteUserById(id);
        log.info("User deleted successfully with ID: " + id);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/list/by-birth-date-range")
    public ResponseEntity<AllUserByDateRangeResponse> findUsersByBirthDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        log.info("Searching for users born between " + from + " and " + to);
        var users = userService.findUsersByBirthDateRange(from, to);
        log.info("Found " + users.getUsers().size() + " users born in the specified date range.");

        return ResponseEntity.ok(users);
    }
}
