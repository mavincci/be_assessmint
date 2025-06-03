package com.assessmint.be.auth.services.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record ToggleActiveDTO(
        @NotBlank
        @UUID
        String userId
) {
}
