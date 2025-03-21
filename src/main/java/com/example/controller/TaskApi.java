package com.example.controller;

import com.example.model.dto.AssignRequest;
import com.example.model.dto.PriorityUpdateRequest;
import com.example.model.dto.StatusUpdateRequest;
import com.example.model.dto.TaskFilterRequest;
import com.example.model.dto.TaskIdRequest;
import com.example.model.dto.TaskRequest;
import com.example.model.dto.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Task Controller", description = "Task management")
public interface TaskApi {

    @Operation(
            summary = "Fetch all tasks",
            description = "fetches all tasks entities and their data from data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    ResponseEntity<List<TaskResponse>> getAllTasks();

    @Operation(
            summary = "Fetch filtered tasks",
            description = "Fetches tasks based on filtering criteria provided in the request."
    )
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
    ResponseEntity<Page<TaskResponse>> getTasks(TaskFilterRequest request);

    @Operation(
            summary = "Get task by ID",
            description = "Fetches a specific task by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<TaskResponse> getTaskById(TaskIdRequest request);

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    ResponseEntity<TaskResponse> createTask(TaskRequest request);

    @Operation(
            summary = "Update an existing task",
            description = "Updates the details of an existing task."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<TaskResponse> updateTask(TaskRequest request);

    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<Void> deleteTask(TaskIdRequest request);

    @Operation(
            summary = "Update task status",
            description = "Updates the status of a specific task."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<TaskResponse> updateTaskStatus(StatusUpdateRequest request);

    @Operation(
            summary = "Update task priority",
            description = "Updates the priority level of a specific task."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task priority updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    ResponseEntity<TaskResponse> updateTaskPriority(PriorityUpdateRequest request);

    @Operation(
            summary = "Assign task to a user",
            description = "Assigns a task to a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Task or user not found")
    })
    ResponseEntity<TaskResponse> assignTask(AssignRequest request);

    @Operation(
            summary = "Get all tasks by user",
            description = "Get a tasks to a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks get successfully"),
            @ApiResponse(responseCode = "404", description = "Tasks by user not found")
    })
    ResponseEntity<Page<TaskResponse>> getTaskByFilter(TaskFilterRequest request);
}