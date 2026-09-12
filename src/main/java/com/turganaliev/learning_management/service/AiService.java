package com.turganaliev.learning_management.service;

import com.turganaliev.learning_management.model.ChatMessage;
import com.turganaliev.learning_management.model.SenderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${ai.api.key:default-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public AiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String explainWithHistory(List<ChatMessage> history, String newMessage, String systemContext) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        List<Map<String, Object>> contents = new ArrayList<>();

        for (ChatMessage msg : history) {
            String role = msg.getSender() == SenderType.USER ? "user" : "model";
            contents.add(Map.of(
                    "role", role,
                    "parts", List.of(Map.of("text", msg.getContent()))
            ));
        }

        contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", newMessage))
        ));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", contents);
        if (systemContext != null) {
            requestBody.put("system_instruction",
                    Map.of("parts", List.of(Map.of("text", systemContext))));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return extractText(response.getBody());
        } catch (HttpClientErrorException.TooManyRequests e) {
            return "Rate limit reached. Please wait a few seconds before asking again.";
        } catch (RestClientException e) {
            return "The assistant is temporarily unavailable. Please try again shortly.";
        }
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> body) {
        List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) body.get("candidates");
        Map<String, Object> content =
                (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts =
                (List<Map<String, Object>>) content.get("parts");
        return (String) parts.get(0).get("text");
    }
}