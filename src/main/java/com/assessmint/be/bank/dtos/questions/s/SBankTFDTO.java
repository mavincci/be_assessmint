package com.assessmint.be.bank.dtos.questions.s;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SBankTFDTO extends SBankQuestionDTO {
    private final String questionText;

    public SBankTFDTO(UUID id, String questionText) {
        this.setId(id);
        this.questionText = questionText;
    }

    @Override
    public Map<String, Object> getQuestionData() {
        return new HashMap<>() {{
            put("questionText", questionText);
        }};
    }

    public static SBankTFDTO fEntity(BankTrueFalseQuestion entity) {
        return new SBankTFDTO(
                entity.getId(),
                entity.getQuestionText()
        );
    }
}
