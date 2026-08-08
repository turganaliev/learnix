package com.turganaliev.learning_management.controller;

import com.turganaliev.learning_management.dto.ChatRequestDto;
import com.turganaliev.learning_management.dto.ChatResponseDto;
import com.turganaliev.learning_management.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
