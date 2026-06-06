package com.turganaliev.learning_management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    @Value("${ai.api.key:default-key}")
    private String apiKey;

    public String explainText(String text) {
        return "AI response will go here.";
    }
}
