package com.assessmint.be.assessment.dtos.question.add_question;

import com.assessmint.be.assessment.dtos.question.AddQuestionDTO;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.global.controllers.validators.ValidBoolean;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
public class AddMultipleChoiceQuestionDTO extends AddQuestionDTO {
    @NotBlank
    private String questionText;

    @Size(min = 2)
    private List<String> options;

    @Size(min = 1)
    private List<String> answers;

    @AssertTrue(message = "ALL_ANSWERS_MUST_BE_IN_OPTIONS")
    public boolean isAnswersInOptions() {
        return new HashSet<>(options).containsAll(answers);
    }

    @AssertTrue(message = "ANSWERS_AND_OPTIONS_CANNOT_CONTAIN_EMPTY_VALUE")
    public boolean isAnswersAndOptionsNotEmpty() {
        return options.stream().map(String::trim).noneMatch(String::isEmpty)
                && answers.stream().map(String::trim).noneMatch(String::isEmpty);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }
}
