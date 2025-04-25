package com.assessmint.be.assessment.entities;

import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class AssessmentSection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    private Assessment assessment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentSection that = (AssessmentSection) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
