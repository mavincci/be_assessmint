package com.assessmint.be.assessment.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID attemptId;
    private UUID assessmentId;

    @Builder.Default
    private int successCount = 0;

    @Builder.Default
    private int failureCount = 0;

    @Builder.Default
    private int skippedCount = 0;
}
