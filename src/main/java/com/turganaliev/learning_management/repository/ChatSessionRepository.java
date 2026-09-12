package com.turganaliev.learning_management.repository;

import com.turganaliev.learning_management.model.ChatSession;
import com.turganaliev.learning_management.model.Section;
import com.turganaliev.learning_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);
    Optional<ChatSession> findByUserAndSection(User user, Section section);
    List<ChatSession> findByUserAndSectionIsNullOrderByCreatedAtDesc(User user);
}
