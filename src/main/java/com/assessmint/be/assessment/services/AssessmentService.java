package com.assessmint.be.assessment.services;

import com.assessmint.be.assessment.dtos.assessment.*;
import com.assessmint.be.assessment.dtos.assessment_section.CreateAssessmentSectionDTO;
import com.assessmint.be.assessment.dtos.assessment_section.SAssessmentSectionDTO;
import com.assessmint.be.assessment.dtos.attempt.AttemptDTO;
import com.assessmint.be.assessment.dtos.attempt.StartAssessmentDTO;
import com.assessmint.be.assessment.dtos.question.AddQuestionFromBankDTO;
import com.assessmint.be.assessment.dtos.question.q.AddQuestionDTO;
import com.assessmint.be.assessment.dtos.question.q.QuestionDTO;
import com.assessmint.be.assessment.dtos.question.mcq.QuestionMultipleChoiceDTO;
import com.assessmint.be.assessment.dtos.question.tf.QuestionTrueFalseDTO;
import com.assessmint.be.assessment.dtos.question.mcq.AddMultipleChoiceQuestionDTO;
import com.assessmint.be.assessment.dtos.question.tf.AddTrueFalseQuestionDTO;
import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.entities.AssessmentSection;
import com.assessmint.be.assessment.entities.Invitation;
import com.assessmint.be.assessment.entities.question_attempts.Attempt;
import com.assessmint.be.assessment.entities.questions.MCQAnswer;
import com.assessmint.be.assessment.entities.questions.MultipleChoiceQuestion;
import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.assessment.repositories.AssessmentRepository;
import com.assessmint.be.assessment.repositories.AssessmentSectionRepository;
import com.assessmint.be.assessment.repositories.AttemptRepository;
import com.assessmint.be.assessment.repositories.InvitationRepository;
import com.assessmint.be.assessment.repositories.questions.MCQAnswerRepository;
import com.assessmint.be.assessment.repositories.questions.MultipleChoiceQuestionRepository;
import com.assessmint.be.assessment.repositories.questions.TrueFalseQuestionRepository;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.entities.helpers.AuthRole;
import com.assessmint.be.bank.entities.Bank;
import com.assessmint.be.bank.entities.questions.BankMCQAnswer;
import com.assessmint.be.bank.entities.questions.BankMultipleChoiceQuestion;
import com.assessmint.be.bank.entities.questions.BankQuestion;
import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;
import com.assessmint.be.bank.repositories.BankRepository;
import com.assessmint.be.bank.repositories.questions.BankQuestionRepository;
import com.assessmint.be.global.Utils;
import com.assessmint.be.global.configurations.DateConstants;
import com.assessmint.be.global.exceptions.ConflictException;
import com.assessmint.be.global.exceptions.NotAuthorizedException;
import com.assessmint.be.global.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final AssessmentSectionRepository assessmentSectionRepository;
    private final AttemptRepository attemptRepository;
    private final BankRepository bankRepository;
    private final BankQuestionRepository bankQuestionRepository;
    private final InvitationRepository invitationRepository;

    private final TrueFalseQuestionRepository trueFalseQuestionRepository;
    private final MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    private final MCQAnswerRepository mcqAnswerRepository;

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

    public AssessmentDTO getAssessmentById(UUID id, AuthUser user) {
        final var _assessment = _getAssessmentById(id);

        if (user.hasRole(AuthRole.ADMIN))
            return AssessmentDTO.fromEntity(_assessment);

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");

        final Optional<Invitation> invitation = invitationRepository.findById(id);

        if (_assessment.getIsPublic())
            return AssessmentDTO.fromEntity(_assessment);

        if (invitation.isPresent()
                && invitation.get().getEmails().contains(user.getEmail()))
            return AssessmentDTO.fromEntity(_assessment);

        throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");
    }

    public SAssessmentSectionDTO addSection(CreateAssessmentSectionDTO reqDto, AuthUser user) {
        final var _assessment = _getAssessmentById(UUID.fromString(reqDto.assessmentId()));

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("NOT_AUTHORIZED");

        if (_assessment.getIsPublished())
            throw new ConflictException("ASSESSMENT_ALREADY_PUBLISHED");

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

    @Transactional
    public QuestionDTO addQuestion(AddQuestionDTO reqDto, AuthUser user) {
        final var _section = assessmentSectionRepository
                .findById(UUID.fromString(reqDto.sectionId))
                .orElseThrow(() -> new NotFoundException("SECTION_NOT_FOUND"));

        final var _assessment = _section.getAssessment();

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");

        if (_assessment.getIsPublished() != null && _assessment.getIsPublished())
            throw new ConflictException("ASSESSMENT_ALREADY_PUBLISHED");

        if (_section.getQuestionType() != reqDto.getQuestionType())
            throw new NotAuthorizedException("QUESTION_TYPE_MISMATCH");

        return switch (_section.getQuestionType()) {
            case TRUE_OR_FALSE -> handleTrueFalseQuestion((AddTrueFalseQuestionDTO) reqDto, _section);
            case MULTIPLE_CHOICE -> handleMultipleChoiceQuestion((AddMultipleChoiceQuestionDTO) reqDto, _section);
        };
    }

    @Transactional
    public QuestionDTO addQuestionFromBank(@Valid AddQuestionFromBankDTO reqDto, AuthUser user) {
        final UUID sectionId = UUID.fromString(reqDto.sectionId());

        final AssessmentSection section = assessmentSectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("SECTION_NOT_FOUND"));
        final Assessment assessment = section.getAssessment();

        final UUID bankId = UUID.fromString(reqDto.bankId());

        final Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new NotFoundException("BANK_NOT_FOUND"));

        if (bank.getQuestionType() != section.getQuestionType())
            throw new ConflictException("BANK_SECTION_QUESTION_TYPE_MISMATCH");

        final UUID questionId = UUID.fromString(reqDto.questionId());

        final BankQuestion bankQuestion = bank.getQuestions().stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("BANK_QUESTION_NOT_FOUND"));

        if (bankQuestion.getQuestionType() == QuestionType.TRUE_OR_FALSE) {
            final var question = (BankTrueFalseQuestion) bankQuestion;
            return handleTrueFalseQuestion(
                    new AddTrueFalseQuestionDTO(
                            question.getQuestionText(),
                            question.isAnswer() ? "true" : "false"
                    ),
                    section
            );
        } else if (bankQuestion.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            final var question = (BankMultipleChoiceQuestion) bankQuestion;
            return handleMultipleChoiceQuestion(
                    new AddMultipleChoiceQuestionDTO(
                            question.getQuestionText(),
                            question.getOptions().stream().map(BankMCQAnswer::getAnswerText).toList(),
                            question.getOptions().stream()
                                    .filter(o -> question.getAnswers().contains(o.getId()))
                                    .map(BankMCQAnswer::getAnswerText)
                                    .toList()
                    ),
                    section
            );
        }

        return null;
    }

    public QuestionDTO handleTrueFalseQuestion(AddTrueFalseQuestionDTO reqDto, AssessmentSection _section) {
        final var answer = Utils.parseBoolean(reqDto.answer);
        String questionText = reqDto.questionText.trim();

        final var tempQuestion = TrueFalseQuestion.builder()
                .questionText(questionText)
                .answer(answer)
                .build();

        tempQuestion.setSection(_section);

        _section.addQuestion(tempQuestion);

        final var _savedQuestion = trueFalseQuestionRepository.save(tempQuestion);
        final var _savedSection = assessmentSectionRepository.save(_section);

        return QuestionTrueFalseDTO.fromEntity(_savedQuestion);
    }

    @Transactional
    public QuestionDTO handleMultipleChoiceQuestion(AddMultipleChoiceQuestionDTO reqDto, AssessmentSection _section) {
        final List<String> options = reqDto.getOptions().stream().map(String::trim).distinct().toList();
        final List<String> answers = reqDto.getAnswers().stream().map(String::trim).distinct().toList();

        String questionText = reqDto.getQuestionText().trim();

        final List<MCQAnswer> answerOptions = options.stream()
                .map(ans -> MCQAnswer.builder()
                        .answerText(ans)
                        .build())
                .toList();

        final var savedAnswerOptions = mcqAnswerRepository.saveAll(answerOptions);

        final List<UUID> answerIds = savedAnswerOptions.stream()
                .filter(ans -> answers.contains(ans.getAnswerText()))
                .map(MCQAnswer::getId)
                .toList();

        final var tempQuestion = MultipleChoiceQuestion.builder()
                .questionText(questionText)
                .options(savedAnswerOptions)
                .answers(answerIds)
                .build();

        tempQuestion.setSection(_section);

        _section.addQuestion(tempQuestion);

        final var _savedQuestion = multipleChoiceQuestionRepository.save(tempQuestion);
        final var _savedSection = assessmentSectionRepository.save(_section);

        return QuestionMultipleChoiceDTO.fromEntity(_savedQuestion);
    }

    public AssessmentSettingDTO updateSettings(UpdateSettingDTO reqDto, AuthUser user) {
        final var _assessment = _getAssessmentById(UUID.fromString(reqDto.assessmentId()));

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("NOT_AUTHORIZED");

        if (_assessment.getIsPublished() != null && _assessment.getIsPublished())
            throw new ConflictException("ASSESSMENT_ALREADY_PUBLISHED");

        final var startDateTime = LocalDateTime.parse(reqDto.startDateTime(), DateConstants.dateTimeFormatter);
        final var endDateTime = LocalDateTime.parse(reqDto.endDateTIme(), DateConstants.dateTimeFormatter);
        final var now = LocalDateTime.now();

        if (startDateTime.isBefore(now))
            throw new ConflictException("START_DATE_MUST_BE_IN_THE_FUTURE");

        if (endDateTime.isBefore(now))
            throw new ConflictException("END_DATE_MUST_BE_IN_THE_FUTURE");

        if (endDateTime.isBefore(startDateTime))
            throw new ConflictException("END_DATE_MUST_BE_AFTER_START_DATE");

        final var duration = Integer.parseInt(reqDto.duration());
        final var maxAttempts = Integer.parseInt(reqDto.maxAttempts());
        final var isPublic = Utils.parseBoolean(reqDto.isPublic());

        _assessment.setStartDateTime(startDateTime);
        _assessment.setEndDateTime(endDateTime);
        _assessment.setDuration(duration);
        _assessment.setMaxAttempts(maxAttempts);
        _assessment.setIsPublic(isPublic);

        final var saved = assessmentRepository.save(_assessment);

        return AssessmentSettingDTO.fromEntity(saved);
    }

    public HashMap<String, Object> publish(UUID uuid, AuthUser user) {
        final var _assessment = _getAssessmentById(uuid);

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");

        if (_assessment.getIsPublished() != null && _assessment.getIsPublished())
            throw new ConflictException("ASSESSMENT_ALREADY_PUBLISHED");

        _assessment.setIsPublished(true);
        _assessment.setPublishedAt(LocalDateTime.now());

        final var saved = assessmentRepository.save(_assessment);

        return new HashMap<>() {{
            put("assessmentId", saved.getId());
            put("publishedAt", DateConstants.dateTimeFormatter.format(saved.getPublishedAt()));
            put("isPublished", saved.getIsPublished());
        }};
    }

    //    @PreAuthorize("hasRole('EXAMINEE')")
    public AttemptDTO startAssessment(StartAssessmentDTO reqDto, AuthUser user) {
        final var _assessment = _getAssessmentById(UUID.fromString(reqDto.assessmentId()));

        if (!_assessment.getIsPublished())
            throw new ConflictException("ASSESSMENT_NOT_PUBLISHED");

        if (_assessment.getStartDateTime() != null && _assessment.getStartDateTime().isAfter(LocalDateTime.now()))
            throw new ConflictException("ASSESSMENT_NOT_STARTED_YET");

        if (_assessment.getEndDateTime() != null && _assessment.getEndDateTime().isBefore(LocalDateTime.now()))
            throw new ConflictException("ASSESSMENT_ALREADY_ENDED");

        final var maxAttempts = _assessment.getMaxAttempts();

        if (maxAttempts != null && maxAttempts > 0) {
            final var attempts = attemptRepository
                    .findAllByAssessmentIdAndExamineeId(
                            _assessment.getId(), user.getId());

            if (attempts.size() >= maxAttempts)
                throw new ConflictException("MAX_ATTEMPTS_REACHED");
        }

        final var _attempt = Attempt.builder()
                .assessment(_assessment)
                .examinee(user)
                .build();

        final var endsAt = _assessment.getDuration() == null
                ? null
                : LocalDateTime.now().plusMinutes(_assessment.getDuration());

        _attempt.setEndsAt(endsAt);

        final var savedAttempt = attemptRepository.save(_attempt);

        return AttemptDTO.fromEntity(savedAttempt);
    }

    public List<QuestionDTO> getQuestions(UUID sectionId, AuthUser user) {
        final var section = assessmentSectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("SECTION_NOT_FOUND"));

        final var _assessment = section.getAssessment();

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");

        return section.getQuestions().stream()
                .map(QuestionDTO::fromEntity)
                .toList();
    }


    public Map<String, Object> removeSection(UUID uuid, AuthUser user) {
        final var _section = assessmentSectionRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("SECTION_NOT_FOUND"));

        final var _assessment = _section.getAssessment();

        if (!_assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");

        if (_assessment.getIsPublished() != null && _assessment.getIsPublished())
            throw new ConflictException("ASSESSMENT_ALREADY_PUBLISHED");

        assessmentSectionRepository.delete(_section);

        return Map.of(
                "assessmentId", _assessment.getId(),
                "sectionId", _section.getId()
        );
    }

    public SAssessmentDTO basicInfo(UUID uuid, AuthUser user) {
        final var assessment = _getAssessmentById(uuid);

        if (!assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_AUTHORIZED");

        return SAssessmentDTO.fromEntity(assessment);
    }

}
