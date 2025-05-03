package com.assessmint.be.assessment.dtos.question;

import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import com.assessmint.be.assessment.helpers.QuestionType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrueFalseQuestionDTO extends QuestionDTO {
    private final String questionText;
    private final boolean answer;

    public TrueFalseQuestionDTO(UUID id, String questionText, boolean answer) {
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

    public static TrueFalseQuestionDTO fromEntity(TrueFalseQuestion entity) {
        return new TrueFalseQuestionDTO(
                entity.getId(),
                entity.getQuestionText(),
                entity.isAnswer()
        );
    }
}
