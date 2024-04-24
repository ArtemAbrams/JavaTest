package org.example.javatest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.javatest.controller.UserController;
import org.example.javatest.dto.request.CreateUserRequest;
import org.example.javatest.dto.request.UpdateUserRequest;
import org.example.javatest.dto.response.AllUserByDateRangeResponse;
import org.example.javatest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("email@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }
    @Test
    void testCreateUserValidationFailure() throws Exception {
        CreateUserRequest request = new CreateUserRequest("", "John", "Doe", LocalDate.now().plusDays(1), "123 Main St", "555-1234");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUserValidationEmailFailure() throws Exception {
        CreateUserRequest request = new CreateUserRequest("sdfsdasdasdeqw", "John", "Doe", LocalDate.now().plusDays(1), "123 Main St", "555-1234");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUserValidationFieldNullFailure() throws Exception {
        CreateUserRequest request = new CreateUserRequest("sdfsdasdasdeqw@gmail.com", null, null, LocalDate.now().plusDays(1), "123 Main St", "555-1234");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testUpdateUserValidationFailure() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest(null, "", "John", "Doe", LocalDate.now().plusDays(1), "123 Main St", "555-1234");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testUpdateUser() throws Exception {
        var request = new UpdateUserRequest(1L, "email@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(any(UpdateUserRequest.class));
    }

    @Test
    void testUpdateValidationIdFailureUser() throws Exception {
        var request = new UpdateUserRequest(null, "email@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateValidationEmailFailureUser() throws Exception {
        var request = new UpdateUserRequest(null, "email@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateValidationFieldNullFailureUser() throws Exception {
        var request = new UpdateUserRequest(null, "email@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "555-1234");
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testDeleteUserById() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    void testFindUsersByBirthDateRange() throws Exception {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(1991, 1, 1);
        when(userService.findUsersByBirthDateRange(from, to)).thenReturn(new AllUserByDateRangeResponse(new ArrayList<>()));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/list/by-birth-date-range")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk());

        verify(userService, times(1)).findUsersByBirthDateRange(from, to);
    }
}
