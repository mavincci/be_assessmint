package com.assessmint.be.assessment.dtos.question;

import com.assessmint.be.assessment.dtos.question.add_question.AddTrueFalseQuestionDTO;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "questionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddTrueFalseQuestionDTO.class, name = "TRUE_OR_FALSE")
})
public abstract class AddQuestionDTO {
    @UUID
    public String sectionId;

    public abstract QuestionType getQuestionType();
}
