package com.assessmint.be.assessment.entities.question_attempts;

import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.auth.entities.AuthUser;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private AuthUser examinee;

    @ManyToOne
    private Assessment assessment;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionAttempt> answers = new HashSet<>();

    @Builder.Default
    private Boolean isFinished = false;

    private LocalDateTime finishedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime endsAt;

    public void addAnswer(QuestionAttempt answer) {
        answers.add(answer);
    }
}
