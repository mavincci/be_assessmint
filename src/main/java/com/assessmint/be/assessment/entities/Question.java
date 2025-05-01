package com.assessmint.be.assessment.entities;

import com.assessmint.be.assessment.helpers.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "assessment_question")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING)
@Getter
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    protected String questionText;
    protected Integer points;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", insertable = false, updatable = false)
    protected QuestionType questionType;
}