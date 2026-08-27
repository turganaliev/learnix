package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.AuthResponseDto;
import com.turganaliev.learning_management.dto.UserLoginDto;
import com.turganaliev.learning_management.dto.UserRegistrationDto;
import com.turganaliev.learning_management.dto.UserResponseDto;
import com.turganaliev.learning_management.exception.InvalidPasswordException;
import com.turganaliev.learning_management.exception.UserNameAlreadyExistsException;
import com.turganaliev.learning_management.exception.UserNotFoundException;
import com.turganaliev.learning_management.model.Role;
import com.turganaliev.learning_management.model.User;
import com.turganaliev.learning_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponseDto registerUser(UserRegistrationDto userData) {
        if (userRepository.findByUsername(userData.getUsername()).isPresent()) {
            throw new UserNameAlreadyExistsException("Username already exists!");
        }

        User user = new User();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setEmail(userData.getEmail());
        user.setUsername(userData.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userData.getPassword()));
        user.setRole(Role.STUDENT);
        User savedUser = userRepository.save(user);
        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    @Override
    public AuthResponseDto loginUser(UserLoginDto loginData) {
        User user = userRepository.findByUsername(loginData.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        if (!passwordEncoder.matches(loginData.getPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Invalid password!");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponseDto(token, user.getUsername(), user.getRole());
    }
}
