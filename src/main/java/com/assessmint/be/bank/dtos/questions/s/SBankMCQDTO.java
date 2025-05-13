package com.assessmint.be.bank.dtos.questions.s;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.entities.questions.BankMCQAnswer;
import com.assessmint.be.bank.entities.questions.BankMultipleChoiceQuestion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SBankMCQDTO extends SBankQuestionDTO {
    private final String questionText;

    public SBankMCQDTO(UUID id, String questionText) {
        this.setId(id);
        this.questionText = questionText;
    }

    @Override
    public Map<String, Object> getQuestionData() {
        return new HashMap<>() {{
            put("questionText", questionText);
        }};
    }

    public static SBankMCQDTO fEntity(BankMultipleChoiceQuestion entity) {
        return new SBankMCQDTO(
                entity.getId(),
                entity.getQuestionText()
        );
    }
}
