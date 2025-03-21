package com.example.model.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Integer id,
        String content,
        Integer authorId,
        Integer assigneeId,
        LocalDateTime createdAt
) {
}
