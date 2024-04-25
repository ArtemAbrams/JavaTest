package org.example.javatest;

import org.example.javatest.dto.UserDto;
import org.example.javatest.dto.request.CreateUserRequest;
import org.example.javatest.dto.request.UpdateUserRequest;
import org.example.javatest.exception.AgeLessEighteenException;
import org.example.javatest.exception.UserExistException;
import org.example.javatest.exception.UserNotFoundException;
import org.example.javatest.mapper.UserMapper;
import org.example.javatest.model.User;
import org.example.javatest.repository.UserRepository;
import org.example.javatest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, userMapper);
        ReflectionTestUtils.setField(userService, "minAge", 18);
    }

    @Test
    void testCreateUser_Success() {
        CreateUserRequest request = new CreateUserRequest("user@example.com", "John", "Doe", LocalDate.now().minusYears(20), "123 Main St", "1234567890");
        User mockedUser = new User();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(CreateUserRequest.class))).thenReturn(mockedUser);

        userService.createUser(request);

        verify(userRepository).save(mockedUser);
    }

    @Test
    void testCreateUser_AgeValidationFails() {
        var request = new CreateUserRequest("user@example.com", "John", "Doe", LocalDate.now().minusYears(10), "123 Main St", "1234567890");
        Exception exception = assertThrows(AgeLessEighteenException.class, () -> {
            userService.createUser(request);
        });
        assertTrue(exception.getMessage().contains("User must be at least"));
    }

    @Test
    void testCreateUser_UserExists() {
        var request = new CreateUserRequest("user@example.com", "John", "Doe", LocalDate.now().minusYears(45), "123 Main St", "1234567890");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        Exception exception = assertThrows(UserExistException.class, () -> {
            userService.createUser(request);
        });
        assertTrue(exception.getMessage().contains("User with this email already exists"));
    }

    @Test
    void testFindUsersByBirthDateRange() {
        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();
        when(userRepository.findByBirthDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(new User()));
        when(userMapper.toUserDto(any(User.class))).thenReturn(new UserDto());
        var response = userService.findUsersByBirthDateRange(startDate, endDate);
        assertFalse(response.getUsers().isEmpty());
    }


    @Test
    void testDeleteUserById_Success() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.deleteUserById(userId));
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUserById_NotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(userId);
        });
        assertTrue(exception.getMessage().contains("User with id: " + userId + " not found"));
    }


    @Test
    void testUpdateUser_Success() {
        Long id = 1L;
        String newEmail = "new_email@example.com";
        var request = new UpdateUserRequest(id, newEmail, "John", "Doe", LocalDate.now().minusYears(20), "123 Main St", "1234567890");

        User user = new User();
        user.setId(id);
        user.setEmail("old_email@example.com");
        user.setFirstName("OldName");
        user.setLastName("OldLastName");

        when(userRepository.findById(request.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

        userService.updateUser(request);

        assertEquals(newEmail, user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void testUpdateUser_NotFound() {
        var request = new UpdateUserRequest(1L, "user@example.com", "John", "Doe", LocalDate.now().minusYears(20), "123 Main St", "1234567890");
        when(userRepository.findById(request.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(request);
        });
        assertTrue(exception.getMessage().contains("User with id: " + request.getId() + " not found"));
    }
    @Test
    void testUpdateUser_EmailExists() {
        UpdateUserRequest request = new UpdateUserRequest(1L, "new_email@example.com", "John", "Doe", LocalDate.now().minusYears(20), "123 Main St", "1234567890");
        User user = new User();
        user.setEmail("old_email@example.com");
        when(userRepository.findById(request.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        Exception exception = assertThrows(UserExistException.class, () -> {
            userService.updateUser(request);
        });
        assertTrue(exception.getMessage().contains("User with this email already exists"));
    }

    @Test
    void testUpdateUser_WithMismatchedIdAndEmail_ThrowsNoException() {
        Long id = 1L;
        String newEmail = "mismatch_email@example.com";
        var request = new UpdateUserRequest(id, newEmail, "John", "Doe", LocalDate.now().minusYears(20), "123 Main St", "1234567890");

        User user = new User();
        user.setId(2L);
        user.setEmail("old_email@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.updateUser(request));
        assertEquals(newEmail, user.getEmail());
    }
}
