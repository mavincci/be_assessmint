package com.assessmint.be.bank.entities.questions;

import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bank_multiple_choice")
@DiscriminatorValue("MULTIPLE_CHOICE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankMultipleChoiceQuestion extends BankQuestion {
    private String questionText;

    @OneToMany
    private List<BankMCQAnswer> options;

    @ElementCollection
    private List<UUID> answers;

    {
        questionType = QuestionType.MULTIPLE_CHOICE;
    }
}