package com.example.model.dto;

public record TaskFilterRequest(
        Integer authorId,
        Integer assigneeId,
        int page,
        int size
) {
}
