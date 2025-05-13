package com.assessmint.be.bank.dtos;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.entities.DifficultyLevel;
import com.assessmint.be.global.controllers.validators.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record CreateBankDTO(
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        String description,

        @NotBlank
        @ValidEnum(enumClass = QuestionType.class)
        String questionType,

        @NotBlank
        @UUID
        String categoryId,

        @NotBlank
        @ValidEnum(enumClass = DifficultyLevel.class)
        String difficultyLevel
) {
}
