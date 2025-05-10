package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.global.controllers.validators.ValidBoolean;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DoAnswerTrueFalseDTO extends DoAnswerDTO {
    @ValidBoolean
    @NotBlank
    protected String answer;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TRUE_OR_FALSE;
    }
}
