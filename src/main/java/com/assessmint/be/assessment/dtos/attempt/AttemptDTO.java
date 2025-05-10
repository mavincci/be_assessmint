package com.assessmint.be.assessment.dtos.attempt;

import com.assessmint.be.assessment.entities.Attempt;

import java.time.LocalDateTime;
import java.util.UUID;

public record AttemptDTO(
        UUID id,
        UUID examineeId,
        UUID assessmentId,
        LocalDateTime endsAt,
        LocalDateTime createdAt
) {
    public static AttemptDTO fromEntity(Attempt entity) {
        return new AttemptDTO(
                entity.getId(),
                entity.getExaminee().getId(),
                entity.getAssessment().getId(),
                entity.getEndsAt(),
                entity.getCreatedAt()
        );
    }
}
