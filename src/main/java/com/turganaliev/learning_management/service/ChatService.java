package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.ChatResponseDto;

public interface ChatService {
    ChatResponseDto chat(String message, Long sessionId);
}
