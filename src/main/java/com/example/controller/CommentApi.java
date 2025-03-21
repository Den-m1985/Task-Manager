package com.example.controller;

import com.example.model.dto.CommentRequest;
import com.example.model.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Comment Controller", description = "Управление комментариями к задачам")
public interface CommentApi {

    @Operation(
            summary = "Add a comment",
            description = "Добавляет комментарий к задаче"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request);

    @Operation(
            summary = "Get comments for a task",
            description = "Возвращает список комментариев для указанной задачи"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "400", description = "Некорректный идентификатор задачи")
    })
    ResponseEntity<List<CommentResponse>> getTaskComments(@RequestBody CommentRequest request);

    @Operation(
            summary = "Delete a task",
            description = "Удаляет задачу и все связанные комментарии (только для ролей ADMIN и SUPER_ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для удаления"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    ResponseEntity<Void> deleteTask(@RequestBody CommentRequest request);
}
