package com.assessmint.be.bank.repositories.questions;

import com.assessmint.be.bank.entities.questions.BankMultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankMCQRepository extends JpaRepository<BankMultipleChoiceQuestion, UUID> {
}
