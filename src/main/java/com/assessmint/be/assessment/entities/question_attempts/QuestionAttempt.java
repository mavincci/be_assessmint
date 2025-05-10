package com.assessmint.be.assessment.entities.question_attempts;

import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class QuestionAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

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
