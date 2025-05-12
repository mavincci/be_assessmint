package com.assessmint.be.assessment.dtos.question.q;

import com.assessmint.be.assessment.dtos.question.mcq.SMultipleChoiceQuestionDTO;
import com.assessmint.be.assessment.dtos.question.tf.STrueFalseQuestionDTO;
import com.assessmint.be.assessment.entities.questions.Question;
import com.assessmint.be.assessment.entities.questions.MultipleChoiceQuestion;
import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
public abstract class SQuestionDTO {
    private UUID id;

    public abstract Map<String, Object> getQuestionData();

    public static SQuestionDTO fromEntity(Question entity) {
        return switch (entity.getQuestionType()) {
            case TRUE_OR_FALSE -> STrueFalseQuestionDTO.fromEntity((TrueFalseQuestion) entity);
            case MULTIPLE_CHOICE -> SMultipleChoiceQuestionDTO.fromEntity(
                    (MultipleChoiceQuestion) entity);
        };
    }
}
