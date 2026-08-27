package com.turganaliev.learning_management.controller;

import com.turganaliev.learning_management.dto.AuthResponseDto;
import com.turganaliev.learning_management.dto.UserLoginDto;
import com.turganaliev.learning_management.dto.UserRegistrationDto;
import com.turganaliev.learning_management.dto.UserResponseDto;
import com.turganaliev.learning_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto userData) {
        UserResponseDto newUser = userService.registerUser(userData);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto loginData) {
        AuthResponseDto user = userService.loginUser(loginData);
        return ResponseEntity.ok(user);
    }
}