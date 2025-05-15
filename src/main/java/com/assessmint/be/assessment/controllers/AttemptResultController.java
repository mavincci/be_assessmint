package com.assessmint.be.assessment.controllers;

import com.assessmint.be.assessment.dtos.attempt.AttemptResultDTO;
import com.assessmint.be.assessment.services.AttemptService;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assessments/attempts/result")
@RequiredArgsConstructor
public class AttemptResultController {
    private final AttemptService attemptService;

    @PostMapping("/fetch_result")
    public ResponseEntity<APIResponse<AttemptResultDTO>> processResult(
            @Valid @RequestBody AttemptResultDTO reqdto,
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "RESULT_FETCH_SUCCESS",
                attemptService.fetchResult(reqdto, user.getId())
        );
    }
}
