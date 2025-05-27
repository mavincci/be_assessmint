package com.assessmint.be.ai.dtos;

import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.RequestBody;

public record GenerateQuestionsDTO(
        @Size(min = 1, max = 10000, message = "Message must be between 1 and 10000 characters")
        @NotBlank @RequestBody String topic,

        @Positive(message = "Number of questions must be positive")
        @Max(value = 10, message = "Only a maximum of 10 questions are allowed")
        @RequestBody int numberOfQuestions
) {
}
