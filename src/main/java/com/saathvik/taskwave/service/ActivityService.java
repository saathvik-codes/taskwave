package com.saathvik.taskwave.service;

import com.saathvik.taskwave.entity.ActivityEvent;
import com.saathvik.taskwave.repository.ActivityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityEventRepository activityEventRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void publish(UUID boardId, UUID taskId, String eventType, String description) {
        ActivityEvent event = ActivityEvent.builder()
                .boardId(boardId)
                .taskId(taskId)
                .eventType(eventType)
                .description(description)
                .occurredAt(Instant.now())
                .build();
        activityEventRepository.save(event);
        messagingTemplate.convertAndSend("/topic/boards/" + boardId, event);
    }

    public List<ActivityEvent> history(UUID boardId) {
        return activityEventRepository.findByBoardIdOrderByOccurredAtDesc(boardId);
    }
}
