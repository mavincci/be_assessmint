package com.assessmint.be.bank.entities.questions;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Table(name = "bank_multiple_choice_answer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BankMCQAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String answerText;
}
