package com.assessmint.be.assessment.dtos.assessment;

import com.assessmint.be.assessment.entities.Assessment;

import java.time.LocalDateTime;
import java.util.UUID;

public record SAssessmentDTO(
        UUID id,
        String title,
        String description,
        boolean isPublished,
        SAssessmentSettingDTO settings,
        LocalDateTime createdAt,
        LocalDateTime publishedAt
) {
    public static SAssessmentDTO fromEntity(Assessment entity) {
        return new SAssessmentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getIsPublished(),
                SAssessmentSettingDTO.fromEntity(entity),
                entity.getCreatedAt(),
                entity.getPublishedAt()
        );
    }
}
