package com.assessmint.be.assessment.services;

import com.assessmint.be.assessment.dtos.assessment.CreateAssessmentDTO;
import com.assessmint.be.assessment.dtos.assessment.SAssessmentDTO;
import com.assessmint.be.assessment.dtos.assessment_section.CreateAssessmentSectionDTO;
import com.assessmint.be.assessment.dtos.assessment_section.SAssessmentSectionDTO;
import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.entities.AssessmentSection;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.assessment.repositories.AssessmentRepository;
import com.assessmint.be.assessment.repositories.AssessmentSectionRepository;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.entities.helpers.AuthRole;
import com.assessmint.be.global.exceptions.NotAuthorizedException;
import com.assessmint.be.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final AssessmentSectionRepository assessmentSectionRepository;

    public SAssessmentDTO create(
            CreateAssessmentDTO reqDto,
            AuthUser user
    ) {
        final var _newAssessment = Assessment.builder()
                .title(reqDto.title())
                .description(reqDto.description())
                .owner(user)
                .build();

        final var saved = assessmentRepository.save(_newAssessment);

        return SAssessmentDTO.fromEntity(saved);
    }

    public Assessment _getAssessmentById(UUID id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_FOUND"));
    }

    public SAssessmentDTO getAssessmentById(UUID id, AuthUser user) {
        final var _assessment = _getAssessmentById(id);

        if (user.hasRole(AuthRole.ADMIN))
            return SAssessmentDTO.fromEntity(_assessment);

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("NOT_AUTHORIZED");

        return SAssessmentDTO.fromEntity(_assessment);
    }

    public SAssessmentSectionDTO addSection(CreateAssessmentSectionDTO reqDto, AuthUser user) {
        final var _assessment = _getAssessmentById(UUID.fromString(reqDto.assessmentId()));

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("NOT_AUTHORIZED");

        final var _newSection = AssessmentSection.builder()
                .title(reqDto.title())
                .description(reqDto.description())
                .questionType(QuestionType.valueOf(reqDto.questionType()))
                .assessment(_assessment)
                .build();

        final var saved = assessmentSectionRepository.save(_newSection);

        return SAssessmentSectionDTO.fromEntity(saved);
    }

    public List<SAssessmentSectionDTO> getSections(UUID assessmentId, AuthUser user) {
        final var _assessment = _getAssessmentById(assessmentId);

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("NOT_AUTHORIZED");

        return _assessment.getSections().stream()
                .map(SAssessmentSectionDTO::fromEntity)
                .toList();
    }
}
