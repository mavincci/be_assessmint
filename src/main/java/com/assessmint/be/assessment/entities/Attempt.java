package com.assessmint.be.assessment.entities;

import com.assessmint.be.assessment.helpers.QuestionAttempt;
import com.assessmint.be.auth.entities.AuthUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @ElementCollection
    Set<QuestionAttempt> answers = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    void addAnswer(QuestionAttempt answer) {
        answers.add(answer);
    }
}
