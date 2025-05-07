package com.assessmint.be.assessment.dtos.assessment;

import com.assessmint.be.assessment.entities.Assessment;

import java.util.UUID;

public record SAssessmentDTO(
        UUID id,
        String title,
        String description,
        SAssessmentSettingDTO settings
) {
    public static SAssessmentDTO fromEntity(Assessment entity) {
        return new SAssessmentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                SAssessmentSettingDTO.fromEntity(entity)
        );
    }
}
