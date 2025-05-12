package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.question_attempts.Attempt;
import com.assessmint.be.auth.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttemptRepository extends JpaRepository<Attempt, UUID> {
    List<Attempt> findAllByAssessmentId(UUID assessmentId);

    List<Attempt> findAllByExamineeId(UUID examineeId);

    List<Attempt> findAllByAssessmentIdAndExamineeId(UUID assessmentId, UUID examineeId);

    Optional<Attempt> findFirstByExamineeIdOrderByCreatedAtDesc(UUID examineeId);

    Optional<Attempt> findFirstByAssessmentIdOrderByCreatedAtDesc(UUID assessmentId);

    Optional<Attempt> findFirstByAssessmentIdAndExamineeOrderByCreatedAtDesc(
            UUID assessmentId, AuthUser examinee);
}
