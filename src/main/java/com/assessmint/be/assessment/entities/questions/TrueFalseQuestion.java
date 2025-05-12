package com.assessmint.be.assessment.entities.questions;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_true_false")
@DiscriminatorValue("TRUE_OR_FALSE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrueFalseQuestion extends Question {
    private String questionText;

    private boolean answer;
}
