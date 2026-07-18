package com.saathvik.taskwave.repository;

import com.saathvik.taskwave.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByTaskIdOrderByPostedAtAsc(UUID taskId);
}
