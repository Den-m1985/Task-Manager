package com.example.model.dto;

public record UserResponse(
        Integer id,
        String email,
        String firstName,
        String lastName,
        String role,
        String username,
        String token
) {
}
