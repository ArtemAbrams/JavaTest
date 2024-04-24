package org.example.javatest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import org.example.javatest.dto.request.CreateUserRequest;
import org.example.javatest.dto.request.UpdateUserRequest;
import org.example.javatest.dto.response.AllUserByDateRangeResponse;
import org.example.javatest.exception.AgeLessEighteenException;
import org.example.javatest.exception.InvalidDateRangeException;
import org.example.javatest.exception.UserExistException;
import org.example.javatest.exception.UserNotFoundException;
import org.example.javatest.mapper.UserMapper;
import org.example.javatest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@JBossLog
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${user.registration.min-age}")
    private int minAge;

    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        log.info("Attempting to create user: " + createUserRequest);
        validateAge(createUserRequest.getBirthDate());
        checkIfUserExists(createUserRequest.getEmail());

        var newUser = userMapper.toEntity(createUserRequest);
        userRepository.save(newUser);
        log.info("User created successfully: " + newUser.getId());
    }

    public AllUserByDateRangeResponse findUsersByBirthDateRange(LocalDate from, LocalDate to) {
        validateDateRange(from, to);

        log.info("Finding users between dates: " + from + " and " + to);
        var users = userRepository.findByBirthDateBetween(from,
                        to)
                .stream()
                .map(userMapper::toDto)
                .toList();

        log.info("Found " + users.size() + " users");
        return AllUserByDateRangeResponse.builder()
                .users(users)
                .build();
    }

    public void deleteUserById(Long id) {
        log.info("Attempting to delete user with ID: " + id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

        userRepository.delete(user);
        log.info("User deleted successfully: " + id);
    }

    @Transactional
    public void updateUser(UpdateUserRequest userRequest) {
        log.info("Attempting to update user: " + userRequest.getId());
        var user = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userRequest.getId() + " not found"));

        validateAge(userRequest.getBirthDate());
        if(!Objects.equals(user.getEmail(), userRequest.getEmail())) {
            checkIfUserExists(userRequest.getEmail());
        }

        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setBirthDate(userRequest.getBirthDate());
        user.setAddress(userRequest.getAddress());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        log.info("User updated successfully: " + user.getId());
    }

    private void validateAge(LocalDate birthDate) {
        if (Period.between(birthDate, LocalDate.now()).getYears() < minAge) {
            log.error("User is too young to register. Required age: " + minAge);
            throw new AgeLessEighteenException("User must be at least " + minAge + " years old to register.");
        }
    }

    private void checkIfUserExists(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            log.error("User with email already exists: " + email);
            throw new UserExistException("User with this email already exists.");
        });
    }
    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new InvalidDateRangeException("Both 'from' and 'to' dates must be provided.");
        }
        if (from.isAfter(to)) {
            throw new InvalidDateRangeException("'From' date must not be after 'To' date.");
        }
    }
}
