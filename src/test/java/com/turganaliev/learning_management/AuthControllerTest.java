package com.turganaliev.learning_management;

import com.turganaliev.learning_management.dto.UserRegistrationDto;
import com.turganaliev.learning_management.exception.UserNameAlreadyExistsException;
import com.turganaliev.learning_management.model.User;
import com.turganaliev.learning_management.service.ChatService;
import com.turganaliev.learning_management.service.CourseService;
import com.turganaliev.learning_management.service.JwtService;
import com.turganaliev.learning_management.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ChatService chatService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_Success() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john123");
        dto.setEmail("john@gmail.com");
        dto.setPassword("password123");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        User fakeUser = new User();
        fakeUser.setUsername("john123");

        when(userService.registerUser(any())).thenReturn(fakeUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john123"));
    }

    @Test
    void register_UsernameAlreadyExists() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john123");
        dto.setEmail("john@gmail.com");
        dto.setPassword("password123");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(userService.registerUser(any()))
                .thenThrow(new UserNameAlreadyExistsException("Username already exists!"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isConflict());
    }

    @Test
    void register_Failed() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("");
        dto.setEmail("");
        dto.setPassword("");
        dto.setFirstName("");
        dto.setLastName("");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
