package com.example.model.dto;

public record RegisterRequest(
        String email,
        String password,
        String firstName,
        String lastName
) {
}
