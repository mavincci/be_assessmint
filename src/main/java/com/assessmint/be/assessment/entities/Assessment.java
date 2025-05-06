package com.assessmint.be.assessment.entities;

import com.assessmint.be.auth.entities.AuthUser;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Integer duration;

    private Integer maxAttempts;

    private Boolean isPublic;

    private Boolean isPublished;

    private LocalDateTime publishedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    private AuthUser owner;

    @OneToMany
    @JoinColumn(name = "assessment_id")
    @Builder.Default
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<AssessmentSection> sections = List.of();

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
        Assessment that = (Assessment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
