package com.turganaliev.learning_management.repository;

import com.turganaliev.learning_management.model.ChatMessage;
import com.turganaliev.learning_management.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatSessionOrderByTimestampAsc(ChatSession chatSession);
}
