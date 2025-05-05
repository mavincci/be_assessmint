package com.assessmint.be.assessment.dtos.assessment;

import com.assessmint.be.assessment.dtos.assessment_section.AssessmentSectionDTO;
import com.assessmint.be.assessment.entities.Assessment;

import java.util.List;
import java.util.UUID;

public record AssessmentDTO(
        UUID id,
        String title,
        String description,

        List<AssessmentSectionDTO> sections
) {
    public static AssessmentDTO fromEntity(Assessment entity) {
        return new AssessmentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getSections().stream()
                        .map(AssessmentSectionDTO::fromEntity)
                        .toList()
        );
    }
}
