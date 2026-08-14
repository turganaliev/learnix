package com.turganaliev.learning_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ChatSessionResponseDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
}
