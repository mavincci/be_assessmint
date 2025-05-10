package com.assessmint.be.assessment.entities.questions;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Table(name = "question_multiple_choice_answer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MCQAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String answerText;
}
