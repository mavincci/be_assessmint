package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
}
