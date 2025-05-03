package com.assessmint.be.assessment.repositories.questions;

import com.assessmint.be.assessment.entities.questions.TrueFalseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrueFalseQuestionRepository extends JpaRepository<TrueFalseQuestion, UUID> {
}
