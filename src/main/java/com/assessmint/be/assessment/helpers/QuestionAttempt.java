package com.assessmint.be.assessment.helpers;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
public abstract class QuestionAttempt {
    protected UUID questionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", insertable = false, updatable = false)
    protected QuestionType questionType;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAttempt that = (QuestionAttempt) o;
        return Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(questionId);
    }
}
