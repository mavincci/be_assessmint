package com.assessmint.be.bank.dtos.questions;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.bank.entities.questions.BankMCQAnswer;
import com.assessmint.be.bank.entities.questions.BankMultipleChoiceQuestion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BankMCQDTO extends BankQuestionDTO {
    private final String questionText;
    private final List<BankMCQAnswer> options;
    private final List<UUID> answers;

    public BankMCQDTO(UUID id, String questionText, List<BankMCQAnswer> options, List<UUID> answers) {
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

    public static BankMCQDTO fEntity(BankMultipleChoiceQuestion entity) {
        return new BankMCQDTO(
                entity.getId(),
                entity.getQuestionText(),
                entity.getOptions(),
                entity.getAnswers()
        );
    }
}
