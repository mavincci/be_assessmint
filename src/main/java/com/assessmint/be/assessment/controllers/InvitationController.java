package com.assessmint.be.assessment.controllers;


import com.assessmint.be.assessment.dtos.assessment.SAssessmentDTO;
import com.assessmint.be.assessment.dtos.invitation.AddInvitationDTO;
import com.assessmint.be.assessment.services.InvitationService;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assessments/invitations")
@Tags(value = {
        @Tag(name = "Assessments Invitations", description = "Assessments Invitation API")
})
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping("/invite")
    public ResponseEntity<APIResponse<Map<String, Object>>> invite(
            @Valid @RequestBody AddInvitationDTO reqdto,
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "SEND_INVITATION_SUCCESS",
                invitationService.invite(reqdto, user)
        );
    }

    @GetMapping("/get_invited/{assessment_id}")
    public ResponseEntity<APIResponse<Map<String, Object>>> getInvited(
            @Validated @UUID @PathVariable("assessment_id") String assessmentId,
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "GET_INVITED_SUCCESS",
                invitationService.getInvited(java.util.UUID.fromString(assessmentId), user)
        );
    }

    @GetMapping("/mine")
    public ResponseEntity<APIResponse<List<SAssessmentDTO>>> getMyInvitations(
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "MY_INVITATIONS_FETCH_SUCCESS",
                invitationService.getMyInvitations(user)
        );
    }
}
