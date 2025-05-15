package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.entities.AttemptResult;
import java.util.UUID;

public record AttemptResultDTO(
        UUID attemptId,
        UUID assessmentId,
        int failureCount,
        int successCount,
        int skippedCount
) {
    public static AttemptResultDTO fromEntity(AttemptResult entity) {
        return new AttemptResultDTO(
                entity.getAttemptId(),
                entity.getAssessmentId(),
                entity.getFailureCount(),
                entity.getSuccessCount(),
                entity.getSkippedCount()
        );
    }
}
