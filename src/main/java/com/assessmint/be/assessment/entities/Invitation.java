package com.assessmint.be.assessment.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {
    @Id
    private UUID assessmentId;

    @ElementCollection
    @Builder.Default
    private Set<String> emails = new HashSet<>();

    public void addEmails(List<String> emails) {
        this.emails.addAll(emails);
    }
}
