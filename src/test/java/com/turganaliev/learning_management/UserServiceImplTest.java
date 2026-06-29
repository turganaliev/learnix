package com.turganaliev.learning_management;

import com.turganaliev.learning_management.dto.AuthResponseDto;
import com.turganaliev.learning_management.dto.UserLoginDto;
import com.turganaliev.learning_management.dto.UserRegistrationDto;
import com.turganaliev.learning_management.model.Role;
import com.turganaliev.learning_management.model.User;
import com.turganaliev.learning_management.repository.UserRepository;
import com.turganaliev.learning_management.service.JwtService;
import com.turganaliev.learning_management.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void loginUser_Success() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("john1234");
        loginDto.setPassword("password1234");

        User fakeUser = new User();
        fakeUser.setUsername("john1234");
        fakeUser.setPasswordHash("hashedPassword");

        when(userRepository.findByUsername("john1234")).thenReturn(Optional.of(fakeUser));
        when(passwordEncoder.matches("password1234", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken("john1234")).thenReturn("fake-jwt-token");

        AuthResponseDto result = userService.loginUser(loginDto);
        assertNotNull(result);
        assertEquals("john1234", result.getUsername());
    }

    @Test
    void loginUser_WrongPassword() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("bill123");
        loginDto.setPassword("wrongpassword");

        User fakeUser = new User();
        fakeUser.setUsername("bill123");
        fakeUser.setPasswordHash("hashedPassword");

        when(userRepository.findByUsername("bill123")).thenReturn(Optional.of(fakeUser));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {userService.loginUser(loginDto);});
    }

    @Test
    void registerUser_Success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john123");
        dto.setEmail("john@gmail.com");
        dto.setPassword("password123");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(userRepository.findByUsername("john123")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.registerUser(dto);

        assertNotNull(result);
        assertEquals("john123", result.getUsername());
        assertEquals("hashedPassword", result.getPasswordHash());
        assertEquals(Role.STUDENT, result.getRole());
    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john123");

        when(userRepository.findByUsername("john123")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> {userService.registerUser(dto);});
    }

    @Test
    void findByUsername_Success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("andy123");

        User fakeUser = new User();
        fakeUser.setUsername("andy123");

        when(userRepository.findByUsername("andy123")).thenReturn(Optional.of(fakeUser));

        User result = userService.findByUsername(dto.getUsername());

        assertNotNull(result);
        assertEquals("andy123", result.getUsername());
    }

    @Test
    void findByUsername_UsernameNotFound() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("andy123");

        when(userRepository.findByUsername("andy123")).thenThrow(new RuntimeException("User not found!"));

        assertThrows(RuntimeException.class, () -> {userService.findByUsername(dto.getUsername());});
    }
}
