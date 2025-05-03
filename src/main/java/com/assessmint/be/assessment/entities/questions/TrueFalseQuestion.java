package com.assessmint.be.assessment.entities.questions;

import com.assessmint.be.assessment.entities.Question;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_true_false")
@DiscriminatorValue("TRUE_OR_FALSE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrueFalseQuestion extends Question {
    protected String questionText;

    private boolean answer;
}
