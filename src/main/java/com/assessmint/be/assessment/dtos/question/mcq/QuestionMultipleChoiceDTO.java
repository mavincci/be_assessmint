package com.assessmint.be.assessment.dtos.question.mcq;

import com.assessmint.be.assessment.dtos.question.q.QuestionDTO;
import com.assessmint.be.assessment.entities.questions.MCQAnswer;
import com.assessmint.be.assessment.entities.questions.MultipleChoiceQuestion;
import com.assessmint.be.assessment.helpers.QuestionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuestionMultipleChoiceDTO extends QuestionDTO {
    private final String questionText;
    private final List<MCQAnswer> options;
    private final List<UUID> answers;

    public QuestionMultipleChoiceDTO(UUID id, String questionText, List<MCQAnswer> options, List<UUID> answers) {
        this.setId(id);
        this.questionText = questionText;
        this.options = options;
        this.answers = answers;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public Map<String, Object> getQuestionData() {
        return new HashMap<>() {{
            put("questionText", questionText);
            put("options", options);
            put("answers", answers);
        }};
    }

    public static QuestionMultipleChoiceDTO fromEntity(MultipleChoiceQuestion entity) {
        return new QuestionMultipleChoiceDTO(
                entity.getId(),
                entity.getQuestionText(),
                entity.getOptions(),
                entity.getAnswers()
        );
    }
}
