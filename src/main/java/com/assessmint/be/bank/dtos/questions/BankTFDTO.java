package com.assessmint.be.bank.dtos.questions;

import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankTFDTO extends BankQuestionDTO {
    private final String questionText;
    private final boolean answer;

    public BankTFDTO(UUID id, String questionText, boolean answer) {
        this.setId(id);
        this.questionText = questionText;
        this.answer = answer;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TRUE_OR_FALSE;
    }

    @Override
    public Map<String, Object> getQuestionData() {
        return new HashMap<>() {{
            put("questionText", questionText);
            put("answer", answer);
        }};
    }

    public static BankTFDTO fEntity(BankTrueFalseQuestion entity) {
        return new BankTFDTO(
                entity.getId(),
                entity.getQuestionText(),
                entity.isAnswer()
        );
    }
}
