package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.AttemptResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttemptResultRepository extends JpaRepository<AttemptResult, UUID> {
    Optional<AttemptResult> findByAttemptId(UUID attemptId);

    List<AttemptResult> findAllByAssessmentId(UUID assessmentId);

    Optional<AttemptResult> findFirstByAssessmentIdOrderByCreatedAtDesc(UUID assessmentId);
}
