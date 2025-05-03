package com.assessmint.be.assessment.services;

import com.assessmint.be.assessment.dtos.assessment.CreateAssessmentDTO;
import com.assessmint.be.assessment.dtos.assessment.SAssessmentDTO;
import com.assessmint.be.assessment.dtos.assessment_section.CreateAssessmentSectionDTO;
import com.assessmint.be.assessment.dtos.assessment_section.SAssessmentSectionDTO;
import com.assessmint.be.assessment.dtos.question.AddQuestionDTO;
import com.assessmint.be.assessment.dtos.question.QuestionDTO;
import com.assessmint.be.assessment.dtos.question.add_question.AddTrueFalseQuestionDTO;
import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.entities.AssessmentSection;
import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.assessment.repositories.AssessmentRepository;
import com.assessmint.be.assessment.repositories.AssessmentSectionRepository;
import com.assessmint.be.assessment.repositories.questions.TrueFalseQuestionRepository;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.entities.helpers.AuthRole;
import com.assessmint.be.global.Utils;
import com.assessmint.be.global.exceptions.NotAuthorizedException;
import com.assessmint.be.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final AssessmentSectionRepository assessmentSectionRepository;

    private final TrueFalseQuestionRepository trueFalseQuestionRepository;

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

    @PreAuthorize("hasRole('EXAMINER')")
    public List<SAssessmentDTO> getMyAssessments(AuthUser user) {
        return assessmentRepository.findAllByOwnerId(user.getId()).stream()
                .map(SAssessmentDTO::fromEntity)
                .toList();
    }

    public QuestionDTO addQuestion(AddQuestionDTO reqDto, AuthUser user) {
        final var _section = assessmentSectionRepository
                .findById(UUID.fromString(reqDto.sectionId))
                .orElseThrow(() -> new NotFoundException("SECTION_NOT_FOUND"));

        if (!_section.getAssessment().getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");

        if (_section.getQuestionType() != reqDto.getQuestionType())
            throw new NotAuthorizedException("QUESTION_TYPE_MISMATCH");

        return switch (_section.getQuestionType()) {
            case TRUE_OR_FALSE -> handleTrueFalseQuestion((AddTrueFalseQuestionDTO) reqDto);
            case MULTIPLE_CHOICE -> throw new NotImplementedException("MULTIPLE_CHOICE_NOT_IMPLEMENTED");
        };
    }

    public QuestionDTO handleTrueFalseQuestion(AddTrueFalseQuestionDTO reqDto) {
        final var answer = Utils.parseBoolean(reqDto.answer);
        String questionText = reqDto.questionText.trim();

        final var tempQuestion = TrueFalseQuestion.builder()
                .questionText(questionText)
                .answer(answer)
                .build();

        final var saved = trueFalseQuestionRepository.save(tempQuestion);

        return QuestionDTO.fromEntity(saved);
    }
}
