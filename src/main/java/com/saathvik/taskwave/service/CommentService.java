package com.saathvik.taskwave.service;

import com.saathvik.taskwave.dto.CommentRequest;
import com.saathvik.taskwave.entity.Comment;
import com.saathvik.taskwave.entity.Task;
import com.saathvik.taskwave.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final ActivityService activityService;

    public Comment add(UUID taskId, CommentRequest request) {
        Task task = taskService.get(taskId);

        Comment comment = Comment.builder()
                .taskId(taskId)
                .authorName(request.authorName())
                .text(request.text())
                .postedAt(Instant.now())
                .build();
        Comment saved = commentRepository.save(comment);

        activityService.publish(task.getBoard().getId(), taskId, "COMMENT_ADDED",
                request.authorName() + " commented on " + task.getTitle());
        return saved;
    }

    public List<Comment> forTask(UUID taskId) {
        return commentRepository.findByTaskIdOrderByPostedAtAsc(taskId);
    }
}
