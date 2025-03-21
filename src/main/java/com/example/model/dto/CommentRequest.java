package com.example.model.dto;

public record CommentRequest(
        Integer taskId,
        Integer authorId,
        Integer assigneeId,
        String text
) {
}
