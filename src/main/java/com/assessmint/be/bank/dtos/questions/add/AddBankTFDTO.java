package com.assessmint.be.bank.dtos.questions.add;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.global.controllers.validators.ValidBoolean;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddBankTFDTO extends AddBankQuestionDTO {
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
