package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

@Getter
public class DoAnswerMCQDTO extends DoAnswerDTO {
    @Size(min = 1, message = "Please enter at least one answer")
    protected List<String> answers;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }
}
