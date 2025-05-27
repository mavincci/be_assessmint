package com.assessmint.be.assessment.services;

import com.assessmint.be.assessment.dtos.assessment.SAssessmentDTO;
import com.assessmint.be.assessment.dtos.invitation.AddInvitationDTO;
import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.entities.Invitation;
import com.assessmint.be.assessment.repositories.AssessmentRepository;
import com.assessmint.be.assessment.repositories.InvitationRepository;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.Utils;
import com.assessmint.be.global.exceptions.NotAuthorizedException;
import com.assessmint.be.global.exceptions.NotFoundException;
import com.assessmint.be.notification.EmailService;
import com.assessmint.be.notification.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final AssessmentRepository assessmentRepository;
    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public Map<String, Object> invite(@Valid AddInvitationDTO reqdto, AuthUser user) {
        final UUID assessmentId = UUID.fromString(reqdto.assessmentId());

        final Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_FOUND"));

        if (!assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_ALLOWED");

        final Set<String> emails = reqdto.emails().stream()
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toSet());

        final Optional<Invitation> temp = invitationRepository.findById(assessmentId);

        final Invitation invitation = temp.orElseGet(() -> Invitation.builder()
                .assessmentId(UUID.fromString(reqdto.assessmentId()))
                .build());

        final List<String> newEmails = emails.stream()
                .filter(e -> !invitation.getEmails().contains(e))
                .toList();

        invitation.addEmails(newEmails);
        final Invitation saved = invitationRepository.save(invitation);

        for (final String email : newEmails) {
            final String link = frontendUrl + "/invitation/" + assessmentId;
            final Map<String, Object> model = Map.of(
                    "firstName", Utils.capitalizeName(user.getFirstName()),
                    "lastName", Utils.capitalizeName(user.getLastName()),
                    "assessmentName", assessment.getTitle(),
                    "duration", assessment.getDuration(),
                    "dueDate", assessment.getEndDateTime(),
                    "receiverEmail", email,
                    "assessmentLink", link
            );
            try {
                emailService.sendTemplated(
                        email,
                        "Invitation to Assessment",
                        EmailTemplate.INVITATION.templateName,
                        model
                );
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        return Map.of(
                "assessmentId", assessmentId,
                "emails", newEmails
        );
    }

    public Map<String, Object> getInvited(UUID assessmentId, AuthUser user) {
        final Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new NotFoundException("ASSESSMENT_NOT_FOUND"));

        if (!assessment.getOwner().getId().equals(user.getId()))
            throw new NotAuthorizedException("ASSESSMENT_ACCESS_NOT_ALLOWED");

        final Invitation invitation = invitationRepository.findById(assessmentId)
                .orElseThrow(() -> new NotFoundException("INVITATION_NOT_FOUND"));

        return Map.of(
                "assessmentId", invitation.getAssessmentId(),
                "emails", invitation.getEmails()
        );
    }

    public List<SAssessmentDTO> getMyInvitations(AuthUser user) {
        final List<Invitation> invitations = invitationRepository.findAllByEmailsContaining(user.getEmail());

        final List<Assessment> assessments = assessmentRepository
                .findAllById(invitations.stream()
                        .map(Invitation::getAssessmentId)
                        .toList());

        return assessments.stream().map(SAssessmentDTO::fromEntity).toList();
    }
}
