package com.assessmint.be.assessment.dtos.assessment_section;

import com.assessmint.be.assessment.dtos.question.s.SQuestionDTO;
import com.assessmint.be.assessment.entities.AssessmentSection;
import com.assessmint.be.assessment.helpers.QuestionType;

import java.util.List;
import java.util.UUID;

public record AssessmentSectionDTO(
        UUID id,
        String title,
        String description,
        QuestionType questionType,
        List<SQuestionDTO> questions
) {
    public static AssessmentSectionDTO fromEntity(AssessmentSection entity) {
        System.out.println("Has questions: " + entity.getQuestions().size());
        return new AssessmentSectionDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getQuestionType(),
                entity.getQuestions().stream()
                        .map(SQuestionDTO::fromEntity)
                        .toList()
        );
    }
}
