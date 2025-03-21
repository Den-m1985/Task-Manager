package com.example.service;

import com.example.model.Task;
import com.example.model.User;
import com.example.model.dto.AssignRequest;
import com.example.model.dto.PriorityUpdateRequest;
import com.example.model.dto.StatusUpdateRequest;
import com.example.model.dto.TaskFilterRequest;
import com.example.model.dto.TaskRequest;
import com.example.model.dto.TaskResponse;
import com.example.model.enums.Priority;
import com.example.model.enums.Status;
import com.example.model.map_stract.TaskMapper;
import com.example.repositiry.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::taskToDto)
                .collect(Collectors.toList());
    }

    public Page<TaskResponse> getTasks(TaskFilterRequest request) {
        Pageable pageable = PageRequest.of(request.page(), request.size());
        Page<Task> tasks = taskRepository.findAll(pageable);

        List<TaskResponse> taskResponses = tasks.getContent()
                .stream()
                .map(taskMapper::taskToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(taskResponses, pageable, tasks.getTotalElements());
    }

    public TaskResponse getTask(Integer taskId) {
        Task task = findTaskById(taskId);
        return taskMapper.taskToDto(task);
    }

    public TaskResponse createTask(TaskRequest request) {
        User user = userService.findUserById(request.authorId());
        Task task = taskMapper.taskDtoToEntity(request);
        task.setAuthor(user);
        if (request.assigneeId() != null) {
            User assignee = userService.findUserById(request.assigneeId());
            task.setAssignee(assignee);
        }
        task = taskRepository.save(task);
        return taskMapper.taskToDto(task);
    }

    public TaskResponse updateTask(TaskRequest request) {
        Task task = findTaskById(request.taskId());
        if (request.assigneeId() != null) {
            if (task.getAssignee() == null || !request.assigneeId().equals(task.getAssignee().getId())) {
                User assignee = userService.findUserById(request.assigneeId());
                task.setAssignee(assignee);
            }
        } else if (task.getAssignee() != null) {
            task.setAssignee(null);
        }
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(Priority.valueOf(request.priority()));
        taskRepository.save(task);
        return taskMapper.taskToDto(task);
    }

    public void deleteTask(Integer taskId) {
        Task task = findTaskById(taskId);
        taskRepository.delete(task);
    }

    public TaskResponse updateTaskStatus(StatusUpdateRequest request) {
        Task task = findTaskById(request.taskId());
        task.setStatus(Status.valueOf(request.status()));
        taskRepository.save(task);
        return taskMapper.taskToDto(task);
    }

    public TaskResponse updateTaskPriority(PriorityUpdateRequest request) {
        Task task = findTaskById(request.taskId());
        task.setPriority(Priority.valueOf(request.priority()));
        taskRepository.save(task);
        return taskMapper.taskToDto(task);
    }

    public TaskResponse assignTask(AssignRequest request) {
        User user = userService.findUserById(request.assigneeId());
        Task task = findTaskById(request.taskId());
        task.setAssignee(user);
        task = taskRepository.save(task);
        return taskMapper.taskToDto(task);
    }

    public Task findTaskById(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + taskId + " not found"));
    }

    public Page<TaskResponse> getTasksByFilter(TaskFilterRequest request) {
        Pageable pageable = PageRequest.of(request.page(), request.size());
        Page<Task> tasks;
        if (request.authorId() != null) {
            tasks = taskRepository.findByAuthorId(request.authorId(), pageable);
        } else if (request.assigneeId() != null) {
            tasks = taskRepository.findByAssigneeId(request.assigneeId(), pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        List<TaskResponse> taskResponses = tasks.getContent()
                .stream()
                .map(taskMapper::taskToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(taskResponses, pageable, tasks.getTotalElements());
    }
}
