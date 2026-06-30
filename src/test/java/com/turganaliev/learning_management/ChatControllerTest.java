package com.turganaliev.learning_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turganaliev.learning_management.dto.ChatRequestDto;
import com.turganaliev.learning_management.service.ChatService;
import com.turganaliev.learning_management.service.JwtService;
import com.turganaliev.learning_management.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ChatService chatService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

//    @Test
//    void chat_Unauthorized_NoToken() throws Exception {
//        ChatRequestDto dto = new ChatRequestDto();
//        dto.setMessage("hello");
//
//        mockMvc.perform(post("/api/chat/request")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isForbidden());
//    }

    @Test
    @WithMockUser
    void chat_Authorized_Success() throws Exception {
        ChatRequestDto dto = new ChatRequestDto();
        dto.setMessage("explain photosynthesis");

        when(chatService.chat(any())).thenReturn("AI explanation here");

        mockMvc.perform(post("/api/chat/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("AI explanation here"));
    }

    @Test
    @WithMockUser
    void chat_Authorized_InvalidInput() throws Exception {
        ChatRequestDto dto = new ChatRequestDto();
        dto.setMessage("");

        mockMvc.perform(post("/api/chat/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}