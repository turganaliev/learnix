package com.turganaliev.learning_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequestDto {
    @NotBlank(message = "Message cannot be empty")
    private String message;
}
