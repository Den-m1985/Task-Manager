package com.example.model.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        Integer userId
) {
}
