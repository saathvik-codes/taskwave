package com.saathvik.taskwave.service;

import com.saathvik.taskwave.dto.TaskRequest;
import com.saathvik.taskwave.entity.Board;
import com.saathvik.taskwave.entity.Task;
import com.saathvik.taskwave.entity.TeamMember;
import com.saathvik.taskwave.repository.BoardRepository;
import com.saathvik.taskwave.repository.TaskRepository;
import com.saathvik.taskwave.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ActivityService activityService;

    public Task create(UUID boardId, TaskRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("Board not found: " + boardId));

        TeamMember assignee = null;
        if (request.assigneeId() != null) {
            assignee = teamMemberRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new NoSuchElementException("Team member not found: " + request.assigneeId()));
        }

        Task task = Task.builder()
                .board(board)
                .title(request.title())
                .description(request.description())
                .assignee(assignee)
                .priority(request.priority() != null ? request.priority() : Task.TaskPriority.MEDIUM)
                .build();
        Task saved = taskRepository.save(task);

        activityService.publish(boardId, saved.getId(), "TASK_CREATED", "Task created: " + saved.getTitle());
        return saved;
    }

    public Task get(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
    }

    public Task updateStatus(UUID id, Task.TaskStatus status) {
        Task task = get(id);
        Task.TaskStatus previous = task.getStatus();
        task.setStatus(status);
        Task saved = taskRepository.save(task);

        activityService.publish(saved.getBoard().getId(), saved.getId(), "TASK_STATUS_CHANGED",
                saved.getTitle() + ": " + previous + " -> " + status);
        return saved;
    }

    public List<Task> byBoard(UUID boardId) {
        return taskRepository.findByBoardId(boardId);
    }
}
