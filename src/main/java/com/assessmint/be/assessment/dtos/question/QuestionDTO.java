package com.assessmint.be.assessment.dtos.question;

import com.assessmint.be.assessment.entities.Question;
import com.assessmint.be.assessment.helpers.QuestionType;
import lombok.AllArgsConstructor;
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
}
