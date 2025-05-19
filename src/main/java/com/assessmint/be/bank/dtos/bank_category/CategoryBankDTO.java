package com.assessmint.be.bank.dtos.bank_category;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.entities.Bank;
import com.assessmint.be.bank.entities.DifficultyLevel;

import java.util.UUID;

public record CategoryBankDTO(
        UUID id,
        String name,
        String description,
        QuestionType questionType,
        DifficultyLevel difficultyLevel,
        int noOfQuestions
) {
    public static CategoryBankDTO fromEntity(Bank entity) {
        return new CategoryBankDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getQuestionType(),
                entity.getDifficultyLevel(),
                entity.getQuestions().size()
        );
    }
}
