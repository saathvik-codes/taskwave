package com.saathvik.taskwave.controller;

import com.saathvik.taskwave.dto.BoardRequest;
import com.saathvik.taskwave.entity.ActivityEvent;
import com.saathvik.taskwave.entity.Board;
import com.saathvik.taskwave.entity.Task;
import com.saathvik.taskwave.entity.Team;
import com.saathvik.taskwave.repository.BoardRepository;
import com.saathvik.taskwave.repository.TeamRepository;
import com.saathvik.taskwave.service.ActivityService;
import com.saathvik.taskwave.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams/{teamId}/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final TeamRepository teamRepository;
    private final TaskService taskService;
    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<Board> create(@PathVariable UUID teamId, @Valid @RequestBody BoardRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team not found: " + teamId));
        Board board = Board.builder().team(team).name(request.name()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(boardRepository.save(board));
    }

    @GetMapping("/{boardId}")
    public Board get(@PathVariable UUID teamId, @PathVariable UUID boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("Board not found: " + boardId));
    }

    @GetMapping("/{boardId}/tasks")
    public List<Task> tasks(@PathVariable UUID teamId, @PathVariable UUID boardId) {
        return taskService.byBoard(boardId);
    }

    @GetMapping("/{boardId}/activity")
    public List<ActivityEvent> activity(@PathVariable UUID teamId, @PathVariable UUID boardId) {
        return activityService.history(boardId);
    }
}
