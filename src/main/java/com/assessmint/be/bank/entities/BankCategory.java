package com.assessmint.be.bank.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany
    @Builder.Default
    private List<Bank> banks = new ArrayList<>();

    @CreatedDate
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public void addBank(Bank saved) {
        banks.add(saved);
    }
}
