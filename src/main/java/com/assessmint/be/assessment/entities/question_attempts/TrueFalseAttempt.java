package com.assessmint.be.assessment.entities.question_attempts;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("TRUE_OR_FALSE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrueFalseAttempt extends QuestionAttempt {
    private boolean answer;
}
