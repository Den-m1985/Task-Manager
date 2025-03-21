package com.example.model.dto;

import java.util.List;

public record TaskResponse(
        Integer id,
        String title,
        String description,
        String priority,
        String status,
        Integer authorId,
        Integer assigneeId,
        List<Integer> commentsIds
) {
}
