package com.assessmint.be.assessment.dtos.assessment;

import com.assessmint.be.global.controllers.validators.ValidBoolean;
import com.assessmint.be.global.controllers.validators.ValidDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.UUID;

public record UpdateSettingDTO(
        @UUID
        @NotBlank
        String assessmentId,

        @NotBlank
        @ValidDateTime
        String startDateTime,

        @NotBlank
        @ValidDateTime
        String endDateTIme,

        @NotBlank
        @PositiveOrZero
        String duration,

        @NotBlank
        @PositiveOrZero
        String maxAttempts,

        @NotBlank
        @ValidBoolean
        String isPublic
) {
}
