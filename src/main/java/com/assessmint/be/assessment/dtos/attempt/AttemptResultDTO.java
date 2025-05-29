package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.entities.AttemptResult;
import com.assessmint.be.auth.entities.AuthUser;

import java.util.UUID;

public record AttemptResultDTO(
        UUID attemptId,
        UUID assessmentId,
        String examineeName,
        String examineeEmail,
        int failureCount,
        int successCount,
        int skippedCount
) {
    public static AttemptResultDTO fromEntity(AttemptResult entity, AuthUser exminee) {
        return new AttemptResultDTO(
                entity.getAttemptId(),
                entity.getAssessmentId(),
                String.format("%s %s", exminee.getFirstName(), exminee.getLastName()),
                exminee.getEmail(),
                entity.getFailureCount(),
                entity.getSuccessCount(),
                entity.getSkippedCount()
        );
    }
}
