package com.example.service;

import com.example.model.Comment;
import com.example.model.Task;
import com.example.model.User;
import com.example.model.dto.CommentRequest;
import com.example.model.dto.CommentResponse;
import com.example.model.map_stract.CommentMapper;
import com.example.repositiry.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    public CommentResponse addComment(CommentRequest request) {
        Task task = taskService.findTaskById(request.taskId());
        User author = userService.findUserById(request.authorId());
        User assignee = request.assigneeId() != null ? userService.findUserById(request.assigneeId()) : null;

        Comment comment = new Comment();
        comment.setContent(request.text());
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setAssignee(assignee);
        comment = commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    public List<CommentResponse> getComments(Integer taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    public void deleteTask(Integer commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    public Comment findCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + commentId + " not found"));
    }
}
