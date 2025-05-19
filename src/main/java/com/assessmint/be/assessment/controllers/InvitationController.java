package com.assessmint.be.assessment.controllers;


import com.assessmint.be.assessment.dtos.invitation.AddInvitationDTO;
import com.assessmint.be.assessment.services.InvitationService;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
