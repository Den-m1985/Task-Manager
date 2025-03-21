package com.example.service;

import com.example.bootstrap.RoleSeeder;
import com.example.model.Comment;
import com.example.model.Role;
import com.example.model.Task;
import com.example.model.User;
import com.example.model.dto.AssignRequest;
import com.example.model.dto.PriorityUpdateRequest;
import com.example.model.dto.StatusUpdateRequest;
import com.example.model.dto.TaskFilterRequest;
import com.example.model.dto.TaskRequest;
import com.example.model.dto.TaskResponse;
import com.example.model.enums.Priority;
import com.example.model.enums.RoleEnum;
import com.example.model.enums.Status;
import com.example.repositiry.CommentRepository;
import com.example.repositiry.TaskRepository;
import com.example.repositiry.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TaskServiceTest {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleSeeder roleSeeder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskService taskService;

    private Task task;
    private User author;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        Role role = roleSeeder.findRoleByName(RoleEnum.USER);
        author = new User();
        author.setFirstName("fn");
        author.setLastName("ln");
        author.setEmail("test@test.com");
        author.setRole(role);
        author.setPassword(passwordEncoder.encode("password"));
        author = userService.saveUser(author);

        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        task.setAuthor(author);
        task = taskRepository.save(task);

        Comment comment = new Comment();
        comment.setContent("text");
        comment.setTask(task);
        comment.setAuthor(author);
        comment = commentRepository.save(comment);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        task.setComments(comments);
    }

    @Test
    void testGetAllTasks() {
        List<TaskResponse> result = taskService.getAllTasks();
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).title());
    }

    @Test
    void testGetTasks() {
        TaskFilterRequest request = new TaskFilterRequest(null, null, 0, 10);
        Page<TaskResponse> result = taskService.getTasks(request);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task", result.getContent().get(0).title());
    }

    @Test
    void testGetTask() {
        TaskResponse result = taskService.getTask(task.getId());
        assertEquals("Test Task", result.title());
    }

    @Test
    void testCreateTask() {
        TaskRequest request = new TaskRequest(
                null,
                "New Task",
                "New Description",
                "HIGH",
                author.getId(),
                null
        );
        TaskResponse result = taskService.createTask(request);
        assertNotNull(result.id());
        assertEquals("New Task", result.title());
        assertEquals("New Description", result.description());
        assertEquals("HIGH", result.priority());
    }

    @Test
    void testUpdateTask() {
        TaskRequest request = new TaskRequest(
                task.getId(),
                "Updated Task",
                "Updated Description",
                "LOW",
                author.getId(),
                null
        );
        TaskResponse result = taskService.updateTask(request);
        assertEquals("Updated Task", result.title());
        assertEquals("Updated Description", result.description());
        assertEquals("LOW", result.priority());
    }

    @Test
    void testDeleteTask() {
        taskService.deleteTask(task.getId());
        assertThrows(EntityNotFoundException.class, () -> {
            taskService.getTask(task.getId());
        });
    }

    @Test
    void testUpdateTaskStatus() {
        StatusUpdateRequest request = new StatusUpdateRequest(task.getId(), Status.COMPLETED.name());
        TaskResponse result = taskService.updateTaskStatus(request);

        assertEquals(Status.COMPLETED.name(), result.status());
    }

    @Test
    void testUpdateTaskPriority() {
        PriorityUpdateRequest request = new PriorityUpdateRequest(task.getId(), "LOW");
        TaskResponse result = taskService.updateTaskPriority(request);

        assertEquals("LOW", result.priority());
    }

    @Test
    void testAssignTask() {
        User assignee = new User();
        assignee.setFirstName("Assignee");
        assignee.setLastName("Test");
        assignee.setEmail("assignee@test.com");
        assignee.setRole(roleSeeder.findRoleByName(RoleEnum.USER));
        assignee.setPassword(passwordEncoder.encode("password"));
        assignee = userService.saveUser(assignee);

        AssignRequest request = new AssignRequest(task.getId(), assignee.getId());
        TaskResponse result = taskService.assignTask(request);

        assertEquals(assignee.getId(), result.assigneeId());
    }

    @Test
    void testGetTasksByFilter() {
        TaskFilterRequest request = new TaskFilterRequest(author.getId(), null, 0, 10);
        Page<TaskResponse> result = taskService.getTasksByFilter(request);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task", result.getContent().get(0).title());
    }
}
