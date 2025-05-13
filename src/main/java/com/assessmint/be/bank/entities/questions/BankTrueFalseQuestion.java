package com.assessmint.be.bank.entities.questions;

import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_true_false")
@DiscriminatorValue("TRUE_OR_FALSE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankTrueFalseQuestion extends BankQuestion {
    private String questionText;

    private boolean answer;

    {
        questionType = QuestionType.TRUE_OR_FALSE;
    }
}
