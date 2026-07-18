package com.saathvik.taskwave.controller;

import com.saathvik.taskwave.dto.MemberRequest;
import com.saathvik.taskwave.dto.TeamRequest;
import com.saathvik.taskwave.entity.Team;
import com.saathvik.taskwave.entity.TeamMember;
import com.saathvik.taskwave.repository.TeamMemberRepository;
import com.saathvik.taskwave.repository.TeamRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @PostMapping
    public ResponseEntity<Team> create(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamRepository.save(Team.builder().name(request.name()).build()));
    }

    @GetMapping("/{id}")
    public Team get(@PathVariable UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Team not found: " + id));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<TeamMember> addMember(@PathVariable UUID id, @Valid @RequestBody MemberRequest request) {
        Team team = get(id);
        TeamMember member = TeamMember.builder()
                .team(team)
                .fullName(request.fullName())
                .email(request.email())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMemberRepository.save(member));
    }

    @GetMapping("/{id}/members")
    public List<TeamMember> members(@PathVariable UUID id) {
        return teamMemberRepository.findByTeamId(id);
    }
}
