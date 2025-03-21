package com.example.model.dto;

public record PriorityUpdateRequest(
        Integer taskId,
        String priority
) {
}
