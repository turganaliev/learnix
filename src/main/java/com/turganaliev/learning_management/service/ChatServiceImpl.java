package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.dto.ChatMessageResponseDto;
import com.turganaliev.learning_management.dto.ChatResponseDto;
import com.turganaliev.learning_management.dto.ChatSessionResponseDto;
import com.turganaliev.learning_management.exception.ChatSessionNotFoundException;
import com.turganaliev.learning_management.exception.SectionNotFoundException;
import com.turganaliev.learning_management.exception.UnauthorizedAccessException;
import com.turganaliev.learning_management.exception.UserNotFoundException;
import com.turganaliev.learning_management.model.*;
import com.turganaliev.learning_management.repository.ChatMessageRepository;
import com.turganaliev.learning_management.repository.ChatSessionRepository;
import com.turganaliev.learning_management.repository.SectionRepository;
import com.turganaliev.learning_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SectionRepository sectionRepository;

    @Override
    @Transactional
    public ChatResponseDto chat(String message, Long sessionId, Long sectionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        ChatSession chatSession;
        String systemContext = null;

        if (sectionId != null) {
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found!"));

            systemContext = """
                    You are a learning assistant embedded in a lesson page.
                    The student is currently reading the lesson below.
                    Answer their questions using this material as the primary source.
                    If the answer is not in the material, say so, then answer briefly from general knowledge.
                    Keep answers short and use Markdown.

                    --- LESSON MATERIAL ---
                    %s
                    """.formatted(section.getContent());

            chatSession = chatSessionRepository.findByUserAndSection(user, section)
                    .orElseGet(() -> {
                        ChatSession newSession = new ChatSession();
                        newSession.setTitle(section.getTitle());
                        newSession.setCreatedAt(LocalDateTime.now());
                        newSession.setUser(user);
                        newSession.setSection(section);
                        return chatSessionRepository.save(newSession);
                    });

        } else if (sessionId != null) {
            chatSession = chatSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new ChatSessionNotFoundException("Chat session not found"));

            if (!chatSession.getUser().getId().equals(user.getId())) {
                throw new UnauthorizedAccessException("You do not have access to this chat session");
            }

        } else {
            ChatSession newSession = new ChatSession();
            newSession.setTitle(message.substring(0, Math.min(message.length(), 50)));
            newSession.setCreatedAt(LocalDateTime.now());
            newSession.setUser(user);
            chatSession = chatSessionRepository.save(newSession);
        }

        List<ChatMessage> history = chatMessageRepository.findByChatSessionOrderByTimestampAsc(chatSession);

        ChatMessage userMessage = new ChatMessage();
        userMessage.setContent(message);
        userMessage.setTimestamp(LocalDateTime.now());
        userMessage.setSender(SenderType.USER);
        userMessage.setChatSession(chatSession);
        chatMessageRepository.save(userMessage);

        String aiResponseText = aiService.explainWithHistory(history, message, systemContext);

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

        List<ChatSession> sessions = chatSessionRepository.findByUserAndSectionIsNullOrderByCreatedAtDesc(user);

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
