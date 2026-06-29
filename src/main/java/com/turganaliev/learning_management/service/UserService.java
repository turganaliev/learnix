package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.AuthResponseDto;
import com.turganaliev.learning_management.dto.UserLoginDto;
import com.turganaliev.learning_management.dto.UserRegistrationDto;
import com.turganaliev.learning_management.model.User;

public interface UserService {
    User registerUser(UserRegistrationDto userData);
    AuthResponseDto loginUser(UserLoginDto loginDto);
    User findByUsername(String username);
}
