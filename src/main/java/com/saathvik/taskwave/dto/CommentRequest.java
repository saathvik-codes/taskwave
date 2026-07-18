package com.saathvik.taskwave.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank String authorName,
        @NotBlank String text
) {
}
