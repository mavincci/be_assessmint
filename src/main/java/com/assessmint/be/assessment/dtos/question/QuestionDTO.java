package com.assessmint.be.assessment.dtos.question;

import com.assessmint.be.assessment.entities.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class QuestionDTO<T> {
    private UUID id;

    public static QuestionDTO fromEntity(Question entity) {
        return new QuestionDTO(
                entity.getId()
        );
    }
}
