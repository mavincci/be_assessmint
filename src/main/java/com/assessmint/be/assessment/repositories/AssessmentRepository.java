package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.auth.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    Collection<Assessment> findAllByOwnerId(UUID ownerId);
}
