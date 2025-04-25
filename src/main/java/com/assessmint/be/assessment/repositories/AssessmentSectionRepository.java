package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.AssessmentSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentSectionRepository extends JpaRepository<AssessmentSection, UUID> {
}
