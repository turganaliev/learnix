package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.ChatMessageResponseDto;
import com.turganaliev.learning_management.dto.ChatResponseDto;
import com.turganaliev.learning_management.dto.ChatSessionResponseDto;

import java.util.List;

public interface ChatService {
    ChatResponseDto chat(String message, Long sessionId);
    List<ChatSessionResponseDto> getUserSessions();
    List<ChatMessageResponseDto> getSessionMessages(Long sessionId);
}
