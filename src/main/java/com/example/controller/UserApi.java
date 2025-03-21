package com.example.controller;

import com.example.model.User;
import com.example.model.dto.ChangeUserRoleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "User Controller", description = "Управление пользователями и их ролями")
public interface UserApi {

    @Operation(
            summary = "Get all users",
            description = "Возвращает список всех пользователей (доступно только для ролей ADMIN и SUPER_ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для просмотра списка пользователей")
    })
    ResponseEntity<List<User>> getAllUsers();

    @Operation(
            summary = "Change user role",
            description = "Изменяет роль пользователя (доступно только для роли SUPER_ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль пользователя успешно изменена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для изменения роли пользователя"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    ResponseEntity<User> changeUserRole(@RequestBody ChangeUserRoleDto request);
}
