package com.assessmint.be.bank.dtos.questions.s;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.dtos.questions.BankMCQDTO;
import com.assessmint.be.bank.dtos.questions.BankTFDTO;
import com.assessmint.be.bank.entities.questions.BankMultipleChoiceQuestion;
import com.assessmint.be.bank.entities.questions.BankQuestion;
import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
public abstract class SBankQuestionDTO {
    private UUID id;

    public abstract Map<String, Object> getQuestionData();

    public static SBankQuestionDTO fromEntity(BankQuestion entity) {
        return switch (entity.getQuestionType()) {
            case TRUE_OR_FALSE -> SBankTFDTO.fEntity((BankTrueFalseQuestion) entity);
            case MULTIPLE_CHOICE -> SBankMCQDTO.fEntity((BankMultipleChoiceQuestion) entity);
        };
    }
}
