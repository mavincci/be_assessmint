package com.assessmint.be.assessment.repositories.question_attempts;

import com.assessmint.be.assessment.entities.question_attempts.MCQAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MCQAttemptRepository extends JpaRepository<MCQAttempt, UUID> {
}
