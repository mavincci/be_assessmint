package com.assessmint.be.assessment.dtos.assessment_section;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.global.controllers.validators.ValidEnum;
import com.assessmint.be.global.controllers.validators.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateAssessmentSectionDTO(
        @NotBlank
        @ValidUUID
        String assessmentId,

        @NotBlank
        String title,

        String description,

        @NotEmpty
        @ValidEnum(enumClass = QuestionType.class)
        String questionType
) {
}
