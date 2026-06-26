package com.turganaliev.learning_management.dto;


import com.turganaliev.learning_management.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String username;
    private Role role;
}
