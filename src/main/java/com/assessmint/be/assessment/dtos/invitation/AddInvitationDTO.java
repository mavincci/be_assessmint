package com.assessmint.be.assessment.dtos.invitation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record AddInvitationDTO(
        @NotBlank
        @UUID
        String assessmentId,

        @Size(min = 1, message = "At least one email is required")
        List<@Email(message = "Invalid email") String> emails
) {
}
