package com.saathvik.taskwave.dto;

import com.saathvik.taskwave.entity.Task;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record TaskRequest(
        @NotBlank String title,
        String description,
        UUID assigneeId,
        Task.TaskPriority priority
) {
}
