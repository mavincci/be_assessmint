package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
