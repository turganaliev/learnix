package com.turganaliev.learning_management.dto;

import com.turganaliev.learning_management.model.SenderType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String content;
    private SenderType sender;
    private LocalDateTime timestamp;
}