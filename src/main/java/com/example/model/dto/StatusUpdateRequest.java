package com.example.model.dto;

public record StatusUpdateRequest(
        Integer taskId,
        String status
) {
}
