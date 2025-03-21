package com.example.model.dto;

public record AssignRequest(
        Integer taskId,
        Integer assigneeId
) {
}
