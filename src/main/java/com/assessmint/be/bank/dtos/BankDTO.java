package com.assessmint.be.bank.dtos;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.dtos.bank_category.SBankCategoryDTO;
import com.assessmint.be.bank.entities.Bank;
import com.assessmint.be.bank.entities.DifficultyLevel;

import java.util.UUID;

public record BankDTO(
        UUID id,
        String name,
        String description,
        QuestionType questionType,
        DifficultyLevel difficultyLevel,
        SBankCategoryDTO category
) {
    public static BankDTO fromEntity(Bank entity) {
        return new BankDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getQuestionType(),
                entity.getDifficultyLevel(),
                SBankCategoryDTO.fromEntity(entity.getCategory())
        );
    }
}
