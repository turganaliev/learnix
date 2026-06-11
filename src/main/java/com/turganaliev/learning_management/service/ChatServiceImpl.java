package com.turganaliev.learning_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    public final AiService aiService;

    @Override
    public String chat(String message) {
        return aiService.explainText(message);
    }
}
