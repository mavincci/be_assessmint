package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "questionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DoAnswerTrueFalseDTO.class, name = "TRUE_OR_FALSE")
})
public abstract class DoAnswerDTO {
    @UUID
    @NotBlank
    protected String assessmentId;

    @UUID
    @NotBlank
    protected String sectionId;

    @UUID
    @NotBlank
    protected String questionId;

    public abstract QuestionType getQuestionType();
}
