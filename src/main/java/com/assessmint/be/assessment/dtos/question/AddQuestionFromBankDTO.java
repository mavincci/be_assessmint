package com.assessmint.be.assessment.dtos.question;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record AddQuestionFromBankDTO(
        @NotBlank
        @UUID
        String bankId,

        @NotBlank
        @UUID
        String sectionId,

        @NotBlank
        @UUID
        String questionId
) {
}
