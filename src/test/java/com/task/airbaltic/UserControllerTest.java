package com.task.airbaltic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.airbaltic.controller.UserController;
import com.task.airbaltic.domain.Users;
import com.task.airbaltic.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersRepository usersRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() throws Exception {
        // Arrange
        Users user = new Users();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmailId("john@example.com");

        when(usersRepository.save(any(Users.class))).thenReturn(user);

        // Act and Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.emailId").value("john@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Arrange
        Users existingUser = new Users();
        existingUser.setId(1L);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmailId("john@example.com");

        Users updatedUser = new Users();
        updatedUser.setId(1L);
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Smith");
        updatedUser.setEmailId("john@example.com");

        when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(Users.class))).thenReturn(updatedUser);

        // Act and Assert
        mockMvc.perform(put("/api/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.emailId").value("john@example.com"));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        // Arrange
        Users updatedUser = new Users();
        updatedUser.setId(1L);
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Smith");
        updatedUser.setEmailId("john@example.com");

        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(put("/api/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User not exist with id :1"));
    }
}
