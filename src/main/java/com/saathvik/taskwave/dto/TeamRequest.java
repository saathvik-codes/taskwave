package com.saathvik.taskwave.dto;

import jakarta.validation.constraints.NotBlank;

public record TeamRequest(@NotBlank String name) {
}
