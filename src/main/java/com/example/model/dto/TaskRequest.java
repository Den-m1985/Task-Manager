package com.example.model.dto;

public record TaskRequest(
        Integer taskId,
        String title,
        String description,
        String priority,
        Integer authorId,
        Integer assigneeId
) {}
