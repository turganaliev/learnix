package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.ChatMessageResponseDto;
import com.turganaliev.learning_management.dto.ChatResponseDto;
import com.turganaliev.learning_management.dto.ChatSessionResponseDto;
import com.turganaliev.learning_management.exception.ChatSessionNotFoundException;
import com.turganaliev.learning_management.exception.UnauthorizedAccessException;
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
import java.util.List;
import java.util.stream.Collectors;

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
                    .orElseThrow(() -> new ChatSessionNotFoundException("Chat session not found"));

            if (!chatSession.getUser().getId().equals(user.getId())) {
                throw new UnauthorizedAccessException("You do not have access to this chat session");
            }
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

    @Override
    public List<ChatSessionResponseDto> getUserSessions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        List<ChatSession> sessions = chatSessionRepository.findByUserOrderByCreatedAtDesc(user);

        return sessions.stream()
                .map(sn -> new ChatSessionResponseDto(sn.getId(), sn.getTitle(), sn.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageResponseDto> getSessionMessages(Long sessionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException("Chat session not found!"));

        if (!chatSession.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this chat session");
        }

        List<ChatMessage> messages = chatMessageRepository.findByChatSessionOrderByTimestampAsc(chatSession);

        return messages.stream()
                .map(msg -> new ChatMessageResponseDto(msg.getContent(), msg.getSender(), msg.getTimestamp()))
                .collect(Collectors.toList());
    }
}
