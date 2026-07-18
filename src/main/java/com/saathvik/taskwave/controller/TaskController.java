package com.saathvik.taskwave.controller;

import com.saathvik.taskwave.dto.CommentRequest;
import com.saathvik.taskwave.dto.TaskRequest;
import com.saathvik.taskwave.entity.Comment;
import com.saathvik.taskwave.entity.Task;
import com.saathvik.taskwave.service.CommentService;
import com.saathvik.taskwave.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final CommentService commentService;

    @PostMapping("/api/v1/boards/{boardId}/tasks")
    public ResponseEntity<Task> create(@PathVariable UUID boardId, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(boardId, request));
    }

    @GetMapping("/api/v1/tasks/{id}")
    public Task get(@PathVariable UUID id) {
        return taskService.get(id);
    }

    @PatchMapping("/api/v1/tasks/{id}/status")
    public Task updateStatus(@PathVariable UUID id, @RequestParam Task.TaskStatus status) {
        return taskService.updateStatus(id, status);
    }

    @PostMapping("/api/v1/tasks/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable UUID id, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.add(id, request));
    }

    @GetMapping("/api/v1/tasks/{id}/comments")
    public List<Comment> comments(@PathVariable UUID id) {
        return commentService.forTask(id);
    }
}
