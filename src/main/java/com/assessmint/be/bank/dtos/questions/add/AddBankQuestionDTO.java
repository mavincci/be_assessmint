package com.assessmint.be.bank.dtos.questions.add;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "questionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddBankTFDTO.class, name = "TRUE_OR_FALSE"),
        @JsonSubTypes.Type(value = AddBankMCQDTO.class, name = "MULTIPLE_CHOICE")
})
public abstract class AddBankQuestionDTO {
    @NotBlank
    @UUID
    public String bankId;

    public abstract QuestionType getQuestionType();
}
