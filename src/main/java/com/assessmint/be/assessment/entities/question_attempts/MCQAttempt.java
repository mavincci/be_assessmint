package com.assessmint.be.assessment.entities.question_attempts;

import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MCQAttempt extends QuestionAttempt {
    @ElementCollection
    private List<UUID> answers;

    {
        questionType = QuestionType.MULTIPLE_CHOICE;
    }
}
