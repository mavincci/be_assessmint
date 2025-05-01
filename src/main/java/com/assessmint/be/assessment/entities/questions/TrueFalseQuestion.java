package com.assessmint.be.assessment.entities.questions;

import com.assessmint.be.assessment.entities.Question;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "question_true_false")
@DiscriminatorValue("TRUE_OR_FALSE")
public class TrueFalseQuestion extends Question {
    private boolean answer;
}
