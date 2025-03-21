package com.example.controller;

import com.example.model.Comment;
import com.example.model.dto.*;
import com.example.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    void createTask_ReturnsCreatedTask() throws Exception {
        TaskRequest request = new TaskRequest(null, "Test Task", "Description", "HIGH", 1, null);
        List<Integer> list = List.of(1,2);
        TaskResponse response = new TaskResponse(1, "Test Task", "Description", "HIGH", "PENDING", 1, null, list);

        Mockito.when(taskService.createTask(Mockito.any(TaskRequest.class))).thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTasks_ReturnsTaskList() throws Exception {
        TaskFilterRequest request = new TaskFilterRequest(null, null, 0, 10);
        List<Integer> list = List.of(1,2);
        Page<TaskResponse> response = new PageImpl<>(List.of(
                new TaskResponse(1, "Task 1", "Desc 1", "HIGH", "PENDING", null, null, list),
                new TaskResponse(2, "Task 2", "Desc 2", "LOW", "COMPLETED", null, null, list)
        ));

        Mockito.when(taskService.getTasks(Mockito.any(TaskFilterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/tasks/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser
    void getTaskById_ReturnsTask() throws Exception {
        Integer taskId = 1;
        TaskIdRequest request = new TaskIdRequest(taskId);
        List<Integer> list = List.of(1,2);
        TaskResponse response = new TaskResponse(taskId, "Task 1", "Desc", "MEDIUM", "PENDING", null, null, list);

        Mockito.when(taskService.getTask(taskId)).thenReturn(response);

        mockMvc.perform(post("/tasks/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    @WithMockUser
    void updateTask_ReturnsUpdatedTask() throws Exception {
        TaskRequest request = new TaskRequest(null, "Test Task", "Description", "HIGH", 1, null);
        List<Integer> list = List.of(1,2);
        TaskResponse response = new TaskResponse(request.taskId(), "Updated Task", "New Desc", "LOW", "PENDING", 1, null, list);

        Mockito.when(taskService.updateTask(Mockito.any(TaskRequest.class))).thenReturn(response);

        mockMvc.perform(put("/tasks/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTask_ReturnsNoContent() throws Exception {
        TaskIdRequest request = new TaskIdRequest(1);

        mockMvc.perform(delete("/tasks/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void updateTaskStatus_ReturnsUpdatedTask() throws Exception {
        StatusUpdateRequest request = new StatusUpdateRequest(1, "COMPLETED");
        List<Integer> list = List.of(1,2);
        TaskResponse response = new TaskResponse(request.taskId(), "Task", "Desc", "HIGH", "COMPLETED", null, null, list);

        Mockito.when(taskService.updateTaskStatus(Mockito.any(StatusUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/tasks/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @WithMockUser
    void updateTaskPriority_ReturnsUpdatedTask() throws Exception {
        PriorityUpdateRequest request = new PriorityUpdateRequest(1, "LOW");
        List<Integer> list = List.of(1,2);
        TaskResponse response = new TaskResponse(request.taskId(), "Task", "Desc", "LOW", "PENDING", null, null, list);

        Mockito.when(taskService.updateTaskPriority(Mockito.any(PriorityUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/tasks/priority")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    @Test
    @WithMockUser
    void assignTask_ReturnsUpdatedTask() throws Exception {
        AssignRequest request = new AssignRequest(1, 2);
        List<Integer> list = List.of(1,2);
        TaskResponse response = new TaskResponse(request.taskId(), "Task", "Desc", "HIGH", "PENDING", null, null, list);

        Mockito.when(taskService.assignTask(Mockito.any(AssignRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/tasks/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
