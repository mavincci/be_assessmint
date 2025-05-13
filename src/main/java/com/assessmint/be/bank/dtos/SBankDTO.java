package com.assessmint.be.bank.dtos;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.dtos.bank_category.SBankCategoryDTO;
import com.assessmint.be.bank.dtos.questions.s.SBankQuestionDTO;
import com.assessmint.be.bank.entities.Bank;
import com.assessmint.be.bank.entities.DifficultyLevel;

import java.util.List;
import java.util.UUID;

public record SBankDTO(
        UUID id,
        String name,
        String description,
        QuestionType questionType,
        DifficultyLevel difficultyLevel,
        SBankCategoryDTO category,
        List<SBankQuestionDTO> questions
) {
    public static SBankDTO fromEntity(Bank entity) {
        return new SBankDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getQuestionType(),
                entity.getDifficultyLevel(),
                SBankCategoryDTO.fromEntity(entity.getCategory()),
                entity.getQuestions().stream().map(SBankQuestionDTO::fromEntity).toList()
        );
    }
}
