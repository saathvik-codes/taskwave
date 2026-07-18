package com.saathvik.taskwave.dto;

import jakarta.validation.constraints.NotBlank;

public record BoardRequest(@NotBlank String name) {
}
