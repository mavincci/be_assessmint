package com.assessmint.be.assessment.dtos.assessment_section;

import com.assessmint.be.assessment.entities.AssessmentSection;

import java.util.UUID;

public record SAssessmentSectionDTO(
        UUID id,
        String title,
        String description,
        String questionType
) {
    public static SAssessmentSectionDTO fromEntity(AssessmentSection entity) {
        return new SAssessmentSectionDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getQuestionType().name()
        );
    }
}
