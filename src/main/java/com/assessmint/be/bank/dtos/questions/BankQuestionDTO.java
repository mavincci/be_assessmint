package com.assessmint.be.bank.dtos.questions;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.entities.questions.BankMultipleChoiceQuestion;
import com.assessmint.be.bank.entities.questions.BankQuestion;
import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
public abstract class BankQuestionDTO {
    private UUID id;

    public abstract QuestionType getQuestionType();

    public abstract Map<String, Object> getQuestionData();

    public static BankQuestionDTO fromEntity(BankQuestion entity) {
        return switch (entity.getQuestionType()) {
            case TRUE_OR_FALSE -> BankTFDTO.fEntity((BankTrueFalseQuestion) entity);
            case MULTIPLE_CHOICE -> BankMCQDTO.fEntity((BankMultipleChoiceQuestion) entity);
        };
    }
}
