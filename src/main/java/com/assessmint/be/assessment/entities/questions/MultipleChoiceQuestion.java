package com.assessmint.be.assessment.entities.questions;

import com.assessmint.be.assessment.entities.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "question_multiple_choice")
@DiscriminatorValue("MULTIPLE_CHOICE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceQuestion extends Question {
    private String questionText;

    @OneToMany
    private List<MCQAnswer> options;

    @ElementCollection
    private List<UUID> answers;
}