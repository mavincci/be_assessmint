package com.assessmint.be.assessment.dtos.question.q;

import com.assessmint.be.assessment.dtos.question.tf.QuestionTrueFalseDTO;
import com.assessmint.be.assessment.entities.questions.Question;
import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import com.assessmint.be.assessment.helpers.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
public abstract class QuestionDTO {
    private UUID id;

    public abstract QuestionType getQuestionType();

    public abstract Map<String, Object> getQuestionData();

    public static QuestionDTO fromEntity(Question entity) {
        return switch (entity.getQuestionType()) {
            case TRUE_OR_FALSE -> QuestionTrueFalseDTO.fromEntity((TrueFalseQuestion) entity);
            case MULTIPLE_CHOICE -> null;
        };
    }
}
