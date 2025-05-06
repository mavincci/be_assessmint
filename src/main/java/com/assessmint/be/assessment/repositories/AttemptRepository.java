package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttemptRepository extends JpaRepository<Attempt, UUID> {
    List<Attempt> findAllByAssessmentId(UUID assessmentId);

    List<Attempt> findAllByExamineeId(UUID examineeId);

    List<Attempt> findAllByAssessmentIdAndExamineeId(UUID assessmentId, UUID examineeId);
}
