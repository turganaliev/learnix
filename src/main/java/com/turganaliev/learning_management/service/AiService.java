package com.turganaliev.learning_management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    public String explainText(String text) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", text)
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return extractText(response.getBody());
        } catch (HttpClientErrorException.TooManyRequests e) {
            return "The AI assistant is very busy right now. Please try again in a moment.";
        } catch (RestClientException e) {
            return "Sorry, the AI assistant is temporarily unavailable. Please try again later.";
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