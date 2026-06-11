package com.turganaliev.learning_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatResponseDto {
    private String response;

    public ChatResponseDto(String response) {
        this.response = response;
    }
}
