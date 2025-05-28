package com.assessmint.be.assessment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
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
    private UUID attemptId;

    private UUID assessmentId;

    @Builder.Default
    private int successCount = 0;

    @Builder.Default
    private int failureCount = 0;

    @Builder.Default
    private int skippedCount = 0;

    @Builder.Default
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();
}
