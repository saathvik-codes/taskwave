package com.saathvik.taskwave.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email
) {
}
