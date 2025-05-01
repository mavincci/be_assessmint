package com.assessmint.be.assessment.entities.questions;

import com.assessmint.be.assessment.entities.Question;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "question_multiple_choice")
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceQuestion extends Question {
    private String[] options;
    private Integer[] correctOptionIndices;
}
