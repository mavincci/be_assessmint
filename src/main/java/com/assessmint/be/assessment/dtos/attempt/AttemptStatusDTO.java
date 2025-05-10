package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.dtos.assessment.SAssessmentDTO;
import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.entities.Attempt;
import com.assessmint.be.assessment.entities.question_attempts.QuestionAttempt;

import java.util.List;
import java.util.UUID;

public record AttemptStatusDTO(
        UUID attemptId,
        SAssessmentDTO assessment,
        List<QuestionAttempt> answers
) {
    public static AttemptStatusDTO fromEntity(Attempt entity, Assessment assessment) {
        return new AttemptStatusDTO(
                entity.getId(),
                SAssessmentDTO.fromEntity(assessment),
                entity.getAnswers().stream().toList()

        );
    }
}
