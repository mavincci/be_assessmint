package com.assessmint.be.assessment.dtos.question.tf;

import com.assessmint.be.assessment.dtos.question.q.AddQuestionDTO;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.global.controllers.validators.ValidBoolean;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddTrueFalseQuestionDTO extends AddQuestionDTO {
    @NotBlank
    public String questionText;

    @NotBlank
    @ValidBoolean
    public String answer;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TRUE_OR_FALSE;
    }
}
