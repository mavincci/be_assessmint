package com.assessmint.be.assessment.repositories;

import com.assessmint.be.assessment.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID>{
}
