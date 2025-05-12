package com.assessmint.be.assessment.services;

import com.assessmint.be.assessment.dtos.attempt.AttemptStatusDTO;
import com.assessmint.be.assessment.dtos.attempt.DoAnswerDTO;
import com.assessmint.be.assessment.dtos.attempt.DoAnswerTrueFalseDTO;
import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.entities.question_attempts.Attempt;
import com.assessmint.be.assessment.entities.questions.Question;
import com.assessmint.be.assessment.entities.question_attempts.TrueFalseAttempt;
import com.assessmint.be.assessment.repositories.AssessmentSectionRepository;
import com.assessmint.be.assessment.repositories.AttemptRepository;
import com.assessmint.be.assessment.repositories.QuestionRepository;
import com.assessmint.be.assessment.repositories.question_attempts.TrueFalseAttemptRepository;
import com.assessmint.be.assessment.repositories.questions.TrueFalseQuestionRepository;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.Utils;
import com.assessmint.be.global.exceptions.ConflictException;
import com.assessmint.be.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttemptService {
    private final AssessmentSectionRepository sectionRepository;
    private final QuestionRepository questionRepository;
    private final TrueFalseQuestionRepository trueFalseQuestionRepository;
    private final AttemptRepository attemptRepository;
    private final TrueFalseAttemptRepository trueFalseAttemptRepository;

    @PreAuthorize("hasRole('EXAMINEE')")
    public AttemptStatusDTO doAnswer(DoAnswerDTO reqDto, AuthUser user) {
        final var sectionId = UUID.fromString(reqDto.getSectionId());
        final var assessmentId = UUID.fromString(reqDto.getAssessmentId());
        final var questionId = UUID.fromString(reqDto.getQuestionId());

        final var _section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("SECTION_NOT_FOUND"));

        final var lastAttempt = attemptRepository
                .findFirstByAssessmentIdOrderByCreatedAtDesc(assessmentId)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_STARTED_YET"));

        if (!_section.getAssessment().getId().equals(assessmentId))
            throw new ConflictException("QUESTION_SECTION_MISMATCH");

        final var _question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("QUESTION_NOT_FOUND"));

        if (reqDto.getQuestionType() != _question.getQuestionType())
            throw new ConflictException("QUESTION_TYPE_MISMATCH");

        return switch (reqDto.getQuestionType()) {
            case TRUE_OR_FALSE -> handleTrueFalseAnswer(
                    reqDto,
                    lastAttempt,
                    _section.getAssessment(),
                    _question);
            default -> throw new NotImplementedException();
        };
    }

    private AttemptStatusDTO handleTrueFalseAnswer(DoAnswerDTO reqDto, Attempt lastAttempt, Assessment assessment, Question question) {
        final var req = (DoAnswerTrueFalseDTO) reqDto;

        final var tfAttempt = TrueFalseAttempt.builder()
                .answer(Utils.parseBoolean(req.getAnswer()))
                .build();
        tfAttempt.setQuestionId(question.getId());

        lastAttempt.addAnswer(tfAttempt);

        final var updatedAttempt = attemptRepository.save(lastAttempt);

        return AttemptStatusDTO.fromEntity(updatedAttempt, assessment);
    }

    public AttemptStatusDTO getAssessmentStatus(UUID assessmentId, AuthUser user) {
        final var _assessment = attemptRepository
                .findFirstByAssessmentIdAndExamineeOrderByCreatedAtDesc(
                        assessmentId, user)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_STARTED_YET"));

        final var _assessmentEntity = _assessment.getAssessment();

        return AttemptStatusDTO.fromEntity(_assessment, _assessmentEntity);
    }
}
