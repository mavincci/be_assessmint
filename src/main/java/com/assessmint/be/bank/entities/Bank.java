package com.assessmint.be.bank.entities;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.bank.entities.questions.BankQuestion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    private AuthUser owner;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;

    @ManyToOne
    private BankCategory category;

    @OneToMany
    @Builder.Default
    private List<BankQuestion> questions = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addQuestion(BankQuestion question) {
        questions.add(question);
    }
}
