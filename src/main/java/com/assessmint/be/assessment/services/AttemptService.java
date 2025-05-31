package com.assessmint.be.assessment.services;

import com.assessmint.be.assessment.dtos.attempt.*;
import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.entities.AttemptResult;
import com.assessmint.be.assessment.entities.question_attempts.Attempt;
import com.assessmint.be.assessment.entities.question_attempts.MCQAttempt;
import com.assessmint.be.assessment.entities.questions.MCQAnswer;
import com.assessmint.be.assessment.entities.questions.MultipleChoiceQuestion;
import com.assessmint.be.assessment.entities.questions.Question;
import com.assessmint.be.assessment.entities.question_attempts.TrueFalseAttempt;
import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import com.assessmint.be.assessment.repositories.*;
import com.assessmint.be.assessment.repositories.question_attempts.MCQAttemptRepository;
import com.assessmint.be.assessment.repositories.question_attempts.TrueFalseAttemptRepository;
import com.assessmint.be.assessment.repositories.questions.MCQAnswerRepository;
import com.assessmint.be.assessment.repositories.questions.TrueFalseQuestionRepository;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.Utils;
import com.assessmint.be.global.configurations.DateConstants;
import com.assessmint.be.global.exceptions.ConflictException;
import com.assessmint.be.global.exceptions.NotFoundException;
import com.assessmint.be.notification.EmailService;
import com.assessmint.be.notification.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptService {
    private final AssessmentRepository assessmentRepoisitory;
    private final AssessmentSectionRepository sectionRepository;
    private final QuestionRepository questionRepository;
    private final TrueFalseQuestionRepository trueFalseQuestionRepository;
    private final AttemptRepository attemptRepository;
    private final TrueFalseAttemptRepository trueFalseAttemptRepository;
    private final MCQAttemptRepository mcqAttemptRepository;
    private final MCQAnswerRepository mcqAnswerRepository;
    private final AttemptResultRepository attemptResultRepository;

    private final EmailService emailService;

    //    @PreAuthorize("hasRole('EXAMINEE')")
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
            case MULTIPLE_CHOICE -> handleMCQAnswer(
                    (DoAnswerMCQDTO) reqDto,
                    lastAttempt,
                    _section.getAssessment(),
                    (MultipleChoiceQuestion) _question);
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

    private AttemptStatusDTO handleMCQAnswer(DoAnswerMCQDTO reqDto, Attempt lastAttempt, Assessment assessment, MultipleChoiceQuestion question) {
        try {
            final Set<UUID> answerIds = reqDto.getAnswers().stream()
                    .map(UUID::fromString).collect(Collectors.toSet());

            final List<MCQAnswer> answerList = mcqAnswerRepository.findAllById(answerIds);

            if (answerList.size() != answerIds.size())
                throw new NotFoundException("ANSWER_NOT_FOUND");

            for (final var answer : answerList) {
                if (!question.getOptions().contains(answer))
                    throw new NotFoundException("ANSWER_NOT_FOUND_IN_QUESTION");
            }

            final MCQAttempt temp = MCQAttempt.builder()
                    .answers(new ArrayList<>(answerIds))
                    .build();
            temp.setQuestionId(question.getId());

            final MCQAttempt saved = mcqAttemptRepository.save(temp);

            lastAttempt.addAnswer(temp);

            final var updatedAttempt = attemptRepository.save(lastAttempt);

            return AttemptStatusDTO.fromEntity(updatedAttempt, assessment);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new ConflictException("INVALID_ANSWER_ID");
        }
    }

    public AttemptStatusDTO getAssessmentStatus(UUID assessmentId, AuthUser user) {
        final var _assessment = attemptRepository
                .findFirstByAssessmentIdAndExamineeOrderByCreatedAtDesc(
                        assessmentId, user)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_STARTED_YET"));

        final var _assessmentEntity = _assessment.getAssessment();

        return AttemptStatusDTO.fromEntity(_assessment, _assessmentEntity);
    }

    @Transactional
    public Map<String, Object> finishAssessment(@Valid FinishDTO reqdto, AuthUser user) throws MessagingException {
        final UUID assessmentId = UUID.fromString(reqdto.assessmentId());

        // Retrieve the Attempt with a pessimistic lock
        final Attempt tempAttempt = attemptRepository
                .findFirstByAssessmentIdAndExamineeAndIsFinishedFalseOrderByCreatedAtDesc(assessmentId, user)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_STARTED_YET"));

        // Optional: Double-check the state (though the lock should ensure exclusivity)
        if (tempAttempt.getIsFinished()) {
            throw new IllegalStateException("Assessment already finished");
        }

        // Process answers to calculate results
        final List<Question> questions = tempAttempt.getAssessment()
                .getSections()
                .stream()
                .flatMap(section -> section.getQuestions().stream())
                .toList();

        int successCount = 0;
        int failureCount = 0;
        int skippedCount = 0;

        for (final var ans : tempAttempt.getAnswers()) {
            final Optional<Question> question = questions.stream()
                    .filter(q -> q.getId().equals(ans.getQuestionId()))
                    .findFirst();

            if (question.isEmpty()) {
                skippedCount++;
                continue;
            }

            switch (question.get().getQuestionType()) {
                case TRUE_OR_FALSE -> {
                    final var tfAns = (TrueFalseAttempt) ans;
                    final var tfQuestion = (TrueFalseQuestion) question.get();

                    if (tfAns.isAnswer() == tfQuestion.isAnswer())
                        successCount++;
                    else
                        failureCount++;
                }
                case MULTIPLE_CHOICE -> {
                    final var mcqAns = (MCQAttempt) ans;
                    final var mcqQuestion = (MultipleChoiceQuestion) question.get();

                    final var correctAnswers = mcqQuestion.getAnswers();
                    final var isCorrect = new HashSet<>(correctAnswers).containsAll(mcqAns.getAnswers());

                    if (isCorrect)
                        successCount++;
                    else
                        failureCount++;
                }
                default -> throw new NotImplementedException();
            }
        }

        System.out.println("Success: " + successCount);
        System.out.println("Failed: " + failureCount);
        System.out.println("Skipped: " + skippedCount);

        // Update and save the Attempt
        tempAttempt.setIsFinished(true);
        tempAttempt.setFinishedAt(LocalDateTime.now());
        final Attempt savedAttempt = attemptRepository.save(tempAttempt);

        // Create and save the AttemptResult
        final AttemptResult tempResult = AttemptResult.builder()
                .attemptId(savedAttempt.getId())
                .assessmentId(savedAttempt.getAssessment().getId())
                .successCount(successCount)
                .failureCount(failureCount)
                .skippedCount(skippedCount)
                .build();
        attemptResultRepository.save(tempResult);

        // Send email and return response
        sendResultEmail(
                user, successCount, failureCount, skippedCount,
                DateConstants.localDateFmtr.format(tempResult.getCreatedAt()),
                tempAttempt.getAssessment().getTitle()
        );

        return Map.of(
                "successCount", successCount,
                "failureCount", failureCount,
                "skippedCount", skippedCount,
                "attempt", AttemptStatusDTO.fromEntity(savedAttempt, savedAttempt.getAssessment())
        );
    }

    public void sendResultEmail(
            AuthUser recipient,
            int successCount,
            int failureCount,
            int skippedCount,
            String dateCompleted,
            String assessmentTitle
    ) throws MessagingException {
        emailService.sendTemplated(
                recipient.getEmail(),
                "Assessment Result",
                EmailTemplate.RESULT_ANNOUNCEMENT.templateName,
                Map.of(
                        "recipientEmail", recipient.getEmail(),
                        "candidateName", String.format("%s %s", recipient.getFirstName(), recipient.getLastName()),
                        "successCount", successCount,
                        "failureCount", failureCount,
                        "skippedCount", skippedCount,
                        "dateCompleted", dateCompleted,
                        "timeTaken", 10,
                        "totalQuestions", successCount + failureCount + skippedCount,
                        "assessmentTitle", assessmentTitle
                )
        );
    }

    public AttemptResultDTO fetchResult(@Valid UUID assessmentId, AuthUser user) {
        final AttemptResult result = attemptResultRepository
                .findFirstByAssessmentIdOrderByCreatedAtDesc(assessmentId)
                .orElseThrow(() -> new NotFoundException("ATTEMPT_RESULT_NOT_FOUND"));

        return AttemptResultDTO.fromEntity(result, user);
    }

    public List<AttemptResultDTO> fetchResults(UUID assessmentId, AuthUser user) {
        final var assessment = assessmentRepoisitory.findById(assessmentId)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_FOUND"));

        if (assessment.getOwner().getId() == null || !assessment.getOwner().getId().equals(user.getId())) {
            throw new ConflictException("ASSESSMENT_NOT_OWNED_BY_USER");
        }

        final List<AttemptResult> results = attemptResultRepository.findAllByAssessmentId(assessmentId);

        final List<Attempt> attempts = attemptRepository.findAllById(
                results.stream().map(AttemptResult::getAttemptId).toList()
        );

        return results.stream().map(
                r -> {
                    final Optional<Attempt> tempA = attempts.stream()
                            .filter(a -> a.getId().equals(r.getAttemptId()))
                            .findFirst();

                    return AttemptResultDTO.fromEntity(r, tempA.get().getExaminee());
                }
        ).toList();
    }
}
