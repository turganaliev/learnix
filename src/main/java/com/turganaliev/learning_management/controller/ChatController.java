package com.turganaliev.learning_management.controller;

import com.turganaliev.learning_management.dto.ChatMessageResponseDto;
import com.turganaliev.learning_management.dto.ChatRequestDto;
import com.turganaliev.learning_management.dto.ChatResponseDto;
import com.turganaliev.learning_management.dto.ChatSessionResponseDto;
import com.turganaliev.learning_management.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/request")
    public ResponseEntity<?> request(@Valid @RequestBody ChatRequestDto message) {
        ChatResponseDto response = chatService.chat(message.getMessage(), message.getChatSessionId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions")
    public ResponseEntity<?> sessions() {
        List<ChatSessionResponseDto> userSessions = chatService.getUserSessions();
        return ResponseEntity.ok(userSessions);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<?> messages(@PathVariable Long sessionId) {
        List<ChatMessageResponseDto> userMessages = chatService.getSessionMessages(sessionId);
        return ResponseEntity.ok(userMessages);
    }
}
