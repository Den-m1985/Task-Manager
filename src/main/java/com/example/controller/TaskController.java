package com.example.controller;

import com.example.model.dto.AssignRequest;
import com.example.model.dto.PriorityUpdateRequest;
import com.example.model.dto.StatusUpdateRequest;
import com.example.model.dto.TaskFilterRequest;
import com.example.model.dto.TaskIdRequest;
import com.example.model.dto.TaskRequest;
import com.example.model.dto.TaskResponse;
import com.example.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController implements TaskApi {
    private final TaskService taskService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/list")
    public ResponseEntity<Page<TaskResponse>> getTasks(@RequestBody TaskFilterRequest request) {
        return ResponseEntity.ok(taskService.getTasks(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/detail")
    public ResponseEntity<TaskResponse> getTaskById(@RequestBody TaskIdRequest request) {
        return ResponseEntity.ok(taskService.getTask(request.taskId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(request));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteTask(@RequestBody TaskIdRequest request) {
        taskService.deleteTask(request.taskId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResponse> updateTaskStatus(@RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTaskStatus(request));
    }

    @PatchMapping("/priority")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResponse> updateTaskPriority(@RequestBody PriorityUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTaskPriority(request));
    }

    @PatchMapping("/assign")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResponse> assignTask(@RequestBody AssignRequest request) {
        return ResponseEntity.ok(taskService.assignTask(request));
    }

    @PostMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TaskResponse>> getTaskByFilter(@RequestBody TaskFilterRequest request) {
        return ResponseEntity.ok(taskService.getTasksByFilter(request));
    }
}
