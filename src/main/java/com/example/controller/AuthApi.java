package com.example.controller;

import com.example.model.dto.AuthResponse;
import com.example.model.dto.LoginRequest;
import com.example.model.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication Controller", description = "Управление аутентификацией пользователей")
public interface AuthApi {

    @Operation(
            summary = "User Registration",
            description = "Регистрирует нового пользователя в системе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует")
    })
    ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request);

    @Operation(
            summary = "User Authentication",
            description = "Авторизует пользователя в системе и возвращает JWT токен"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход, токен в ответе"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request);
}
