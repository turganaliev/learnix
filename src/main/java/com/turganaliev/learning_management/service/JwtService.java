package com.turganaliev.learning_management.service;

public interface JwtService {
    String generateToken(String username);
    String extractUsername(String token);
    boolean validateToken(String username, String token);
}
