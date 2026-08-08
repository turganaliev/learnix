package com.turganaliev.learning_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponseDto {
    private String response;
    private Long chatSessionId;
}
