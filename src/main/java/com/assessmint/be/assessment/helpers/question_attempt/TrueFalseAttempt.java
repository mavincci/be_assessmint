package com.assessmint.be.assessment.helpers.question_attempt;

import com.assessmint.be.assessment.helpers.QuestionAttempt;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class TrueFalseAttempt extends QuestionAttempt {
    private boolean answer;
}
