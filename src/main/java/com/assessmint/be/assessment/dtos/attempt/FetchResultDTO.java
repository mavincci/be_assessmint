package com.assessmint.be.assessment.dtos.attempt;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record FetchResultDTO(
        @NotBlank
        @UUID
        String assessmentId
) {
}
