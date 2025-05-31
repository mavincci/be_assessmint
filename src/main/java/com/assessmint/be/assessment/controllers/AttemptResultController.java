package com.assessmint.be.assessment.controllers;

import com.assessmint.be.assessment.dtos.attempt.AttemptResultDTO;
import com.assessmint.be.assessment.dtos.attempt.AttemptResultNoUserDTO;
import com.assessmint.be.assessment.services.AttemptService;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments/attempts/result")
@RequiredArgsConstructor
public class AttemptResultController {
    private final AttemptService attemptService;

    @GetMapping("/fetch_result/{assessmentId}")
    public ResponseEntity<APIResponse<AttemptResultDTO>> processResult(
            @Validated @NotBlank @UUID
            @PathVariable("assessmentId") String assessmentId,
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "RESULT_FETCH_SUCCESS",
                attemptService.fetchResult(java.util.UUID.fromString(assessmentId), user)
        );
    }

    @GetMapping("/fetch_my_results/{assessmentId}")
    public ResponseEntity<APIResponse<List<AttemptResultNoUserDTO>>> fetchMyResult(
            @Validated @NotBlank @UUID
            @PathVariable("assessmentId") String assessmentId,
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "EXAMINEE_RESULT_FETCH_SUCCESS",
                attemptService.fetchMyResult(java.util.UUID.fromString(assessmentId), user)
        );
    }

    @GetMapping("/fetch_results/{assessmentId}")
    public ResponseEntity<APIResponse<List<AttemptResultDTO>>> fetchResults(
            @Validated @NotBlank @UUID
            @PathVariable("assessmentId") String assessmentId,
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "RESULT_FETCH_SUCCESS",
                attemptService.fetchResults(java.util.UUID.fromString(assessmentId), user)
        );
    }
}
