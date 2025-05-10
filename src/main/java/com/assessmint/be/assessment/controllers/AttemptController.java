package com.assessmint.be.assessment.controllers;


import com.assessmint.be.assessment.dtos.attempt.AttemptStatusDTO;
import com.assessmint.be.assessment.dtos.attempt.DoAnswerDTO;
import com.assessmint.be.assessment.services.AttemptService;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/assessments/attempts")
@Tags(value = {
        @Tag(name = "Assessments Attempts", description = "Assessments Attempts API")
})
@RequiredArgsConstructor
public class AttemptController {
    private final AttemptService assessmentService;
    private final AttemptService attemptService;

    @GetMapping("/status/{assessmentId}")
    public ResponseEntity<APIResponse<AttemptStatusDTO>> getAssessmentStatus(
            @PathVariable String assessmentId,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_STATUS_SUCCESS",
                attemptService.getAssessmentStatus(UUID.fromString(assessmentId), user)
        );
    }

    @PostMapping("/do_answer")
    public ResponseEntity<APIResponse<AttemptStatusDTO>> doAnswer(
            @Valid @RequestBody DoAnswerDTO reqDto,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_ANSWER_SUCCESS",
                attemptService.doAnswer(reqDto, user)
        );
    }
}
