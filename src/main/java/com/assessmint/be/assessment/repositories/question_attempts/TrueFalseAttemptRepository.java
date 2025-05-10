package com.assessmint.be.assessment.repositories.question_attempts;

import com.assessmint.be.assessment.entities.question_attempts.TrueFalseAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrueFalseAttemptRepository extends JpaRepository<TrueFalseAttempt, UUID> {
}
