package com.assessmint.be.assessment.controllers;

import com.assessmint.be.assessment.dtos.assessment.*;
import com.assessmint.be.assessment.dtos.assessment_section.CreateAssessmentSectionDTO;
import com.assessmint.be.assessment.dtos.assessment_section.SAssessmentSectionDTO;
import com.assessmint.be.assessment.dtos.attempt.StartAssessmentDTO;
import com.assessmint.be.assessment.dtos.question.AddQuestionDTO;
import com.assessmint.be.assessment.dtos.question.QuestionDTO;
import com.assessmint.be.assessment.services.AssessmentService;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import com.assessmint.be.global.controllers.validators.ValidUUID;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assessments")
@Tags(value = {
        @Tag(name = "Assessments", description = "Assessments API")
})
@RequiredArgsConstructor
public class AssessmentController {
    private final AssessmentService assessmentService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<SAssessmentDTO>> createAssessment(
            @Valid @RequestBody CreateAssessmentDTO reqDto,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.CREATED.value(),
                "ASSESSMENT_CREATE_SUCCESS",
                assessmentService.create(reqDto, user)
        );
    }

    @GetMapping("/get_by_id/{id}")
    public ResponseEntity<APIResponse<AssessmentDTO>> getAssessmentById(
            @ValidUUID(message = "INVALID_UUID_FORMAT") @PathVariable("id") String id,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_GET_SUCCESS",
                assessmentService.getAssessmentById(UUID.fromString(id), user)
        );
    }

    @PostMapping("/add_section")
    public ResponseEntity<APIResponse<SAssessmentSectionDTO>> addSection(
            @Valid @RequestBody CreateAssessmentSectionDTO reqDto,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_SECTION_ADD_SUCCESS",
                assessmentService.addSection(reqDto, user)
        );
    }

    @GetMapping("/get_sections/{assessmentId}")
    public ResponseEntity<APIResponse<List<SAssessmentSectionDTO>>> getSections(
            @ValidUUID(message = "INVALID_UUID_FORMAT")
            @NotBlank
            @NotEmpty
            @PathVariable("assessmentId") String id,

            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_SECTIONS_GET_SUCCESS",
                assessmentService.getSections(UUID.fromString(id), user)
        );
    }

    @GetMapping("/mine")
    public ResponseEntity<APIResponse<List<SAssessmentDTO>>> getMyAssessments(
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENTS_FETCH_SUCCESS",
                assessmentService.getMyAssessments(user)
        );
    }

    @PostMapping("/add_question")
    public ResponseEntity<APIResponse<QuestionDTO>> addQuestion(
            @Valid @RequestBody AddQuestionDTO reqDto,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.CREATED.value(),
                "ASSESSMENT_QUESTION_ADD_SUCCESS",
                assessmentService.addQuestion(reqDto, user)
        );
    }

    @PostMapping("/update_settings")
    public ResponseEntity<APIResponse<AssessmentSettingDTO>> updateSettings(
            @Valid @RequestBody UpdateSettingDTO reqDto,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_SETTINGS_UPDATE_SUCCESS",
                assessmentService.updateSettings(reqDto, user)
        );
    }

    @PostMapping("/publish/{assessmentId}")
    public ResponseEntity<APIResponse<HashMap<String, Object>>> publish(
            @ValidUUID(message = "INVALID_UUID_FORMAT")
            @PathVariable("assessmentId") String assessmentId,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_PUBLISH_SUCCESS",
                assessmentService.publish(UUID.fromString(assessmentId), user)
        );
    }

    @GetMapping("/get_questions/{sectionId}")
    public ResponseEntity<APIResponse<List<QuestionDTO>>> getQuestions(
            @ValidUUID(message = "INVALID_UUID_FORMAT")
            @NotBlank
            @PathVariable("sectionId") String sectionId,

            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ASSESSMENT_QUESTIONS_FETCH_SUCCESS",
                assessmentService.getQuestions(UUID.fromString(sectionId), user)
        );
    }

    @PostMapping("/start_assessment")
    public String startAssessment(
            @Valid @RequestBody StartAssessmentDTO reqDto,
            @AuthenticationPrincipal AuthUser user
    ) {
        return assessmentService.startAssessment(reqDto, user);
    }
}
