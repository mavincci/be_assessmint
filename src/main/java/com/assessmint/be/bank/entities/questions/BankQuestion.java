package com.assessmint.be.bank.entities.questions;

import com.assessmint.be.assessment.entities.AssessmentSection;
import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bank_question")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class BankQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", insertable = false, updatable = false)
    protected QuestionType questionType;

    @ManyToOne
    protected AssessmentSection section;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}