package com.saathvik.taskwave.repository;

import com.saathvik.taskwave.entity.ActivityEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityEventRepository extends MongoRepository<ActivityEvent, String> {
    List<ActivityEvent> findByBoardIdOrderByOccurredAtDesc(UUID boardId);
}
