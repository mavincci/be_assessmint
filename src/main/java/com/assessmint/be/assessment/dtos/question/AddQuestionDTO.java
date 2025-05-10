package com.assessmint.be.assessment.dtos.question;

import com.assessmint.be.assessment.dtos.question.add_question.AddMultipleChoiceQuestionDTO;
import com.assessmint.be.assessment.dtos.question.add_question.AddTrueFalseQuestionDTO;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "questionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddTrueFalseQuestionDTO.class, name = "TRUE_OR_FALSE"),
        @JsonSubTypes.Type(value = AddMultipleChoiceQuestionDTO.class, name = "MULTIPLE_CHOICE")
})
public abstract class AddQuestionDTO {
    @UUID
    public String sectionId;

    public abstract QuestionType getQuestionType();
}
