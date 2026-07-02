package com.turganaliev.learning_management;

import com.turganaliev.learning_management.service.AiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AiService aiService;

    @Test
    void explainText_Success() {
        Map<String, Object> fakeGeminiResponse = Map.of(
                "candidates", List.of(
                        Map.of("content", Map.of(
                                "parts", List.of(
                                        Map.of("text", "Photosynthesis is how plants make food.")
                                )
                        ))
                )
        );

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(fakeGeminiResponse));

        String result = aiService.explainText("explain photosynthesis");

        assertEquals("Photosynthesis is how plants make food.", result);
    }

    @Test
    void explainText_ApiError() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RestClientException("API unavailable"));

        assertThrows(RestClientException.class, () -> {
            aiService.explainText("explain photosynthesis");
        });
    }
}