package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.ChatResponseDto;
import com.turganaliev.learning_management.exception.UserNotFoundException;
import com.turganaliev.learning_management.model.ChatMessage;
import com.turganaliev.learning_management.model.ChatSession;
import com.turganaliev.learning_management.model.SenderType;
import com.turganaliev.learning_management.model.User;
import com.turganaliev.learning_management.repository.ChatMessageRepository;
import com.turganaliev.learning_management.repository.ChatSessionRepository;
import com.turganaliev.learning_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final AiService aiService;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    public ChatResponseDto chat(String message, Long sessionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        ChatSession chatSession;
        if (sessionId == null) {
            ChatSession newSession = new ChatSession();
            newSession.setTitle(message.substring(0, Math.min(message.length(), 50)));
            newSession.setCreatedAt(LocalDateTime.now());
            newSession.setUser(user);
            chatSession = chatSessionRepository.save(newSession);
        } else {
            chatSession = chatSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Chat session not found"));
        }

        ChatMessage userMessage = new ChatMessage();
        userMessage.setContent(message);
        userMessage.setTimestamp(LocalDateTime.now());
        userMessage.setSender(SenderType.USER);
        userMessage.setChatSession(chatSession);
        chatMessageRepository.save(userMessage);

        String aiResponseText = aiService.explainText(message);

        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setContent(aiResponseText);
        aiMessage.setTimestamp(LocalDateTime.now());
        aiMessage.setSender(SenderType.AI);
        aiMessage.setChatSession(chatSession);
        chatMessageRepository.save(aiMessage);

        return new ChatResponseDto(aiResponseText, chatSession.getId());
    }
}
