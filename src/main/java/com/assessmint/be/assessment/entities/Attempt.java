package com.assessmint.be.assessment.entities;

import com.assessmint.be.auth.entities.AuthUser;
import jakarta.persistence.*;
import lombok.*;

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
}
