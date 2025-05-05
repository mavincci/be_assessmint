package com.assessmint.be.assessment.dtos.assessment;

import com.assessmint.be.assessment.entities.Assessment;

import java.time.LocalDateTime;
import java.util.UUID;

public record SAssessmentSettingDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTIme,
        Integer duration,
        Integer maxAttempts,
        Boolean isPublic
) {
    public static SAssessmentSettingDTO fromEntity(Assessment entity) {
        return new SAssessmentSettingDTO(
                entity.getStartDateTime(),
                entity.getEndDateTime(),
                entity.getDuration(),
                entity.getMaxAttempts(),
                entity.getIsPublic()
        );
    }
}
