package com.assessmint.be.assessment.repositories.questions;

import com.assessmint.be.assessment.entities.questions.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MultipleChoiceQuestionRepository
        extends JpaRepository<MultipleChoiceQuestion, UUID> {
}
