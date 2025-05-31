package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.entities.AttemptResult;
import com.assessmint.be.auth.entities.AuthUser;

import java.util.UUID;

public record AttemptResultNoUserDTO(
        UUID attemptId,
        UUID assessmentId,
        int failureCount,
        int successCount,
        int skippedCount
) {
    public static AttemptResultNoUserDTO fromEntity(AttemptResult entity) {
        return new AttemptResultNoUserDTO(
                entity.getAttemptId(),
                entity.getAssessmentId(),
                entity.getFailureCount(),
                entity.getSuccessCount(),
                entity.getSkippedCount()
        );
    }
}
