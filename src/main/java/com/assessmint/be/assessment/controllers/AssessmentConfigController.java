package com.assessmint.be.assessment.controllers;


import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assessments/config")
@Tag(name = "Assessment Config", description = "Assessment Config API")
public class AssessmentConfigController {
    @GetMapping("/question_types")
    public ResponseEntity<APIResponse<QuestionType[]>> getQuestionTypes() {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "QUESTION_TYPES",
                QuestionType.values()
        );
    }
}
