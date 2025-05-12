package com.assessmint.be.assessment.dtos.question.tf;

import com.assessmint.be.assessment.dtos.question.q.SQuestionDTO;
import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class STrueFalseQuestionDTO extends SQuestionDTO {
    private final String questionText;
    private final boolean answer;

    public STrueFalseQuestionDTO(UUID id, String questionText, boolean answer) {
        this.setId(id);
        this.questionText = questionText;
        this.answer = answer;
    }

    @Override
    public Map<String, Object> getQuestionData() {
        return new HashMap<>() {{
            put("questionText", questionText);
            put("answer", answer);
        }};
    }

    public static STrueFalseQuestionDTO fromEntity(TrueFalseQuestion entity) {
        return new STrueFalseQuestionDTO(
                entity.getId(),
                entity.getQuestionText(),
                entity.isAnswer()
        );
    }
}
