package com.assessmint.be.assessment.dtos.assessment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateAssessmentDTO(
        @NotBlank
        @NotEmpty
        String title,

        String description
) {
}
