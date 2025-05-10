package com.assessmint.be.assessment.repositories.questions;

import com.assessmint.be.assessment.entities.questions.MCQAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MCQAnswerRepository extends JpaRepository<MCQAnswer, UUID> {
}
