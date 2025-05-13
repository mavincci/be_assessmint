package com.assessmint.be.bank.repositories.questions;

import com.assessmint.be.bank.entities.questions.BankMCQAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankMCQAnswerRepository extends JpaRepository<BankMCQAnswer, UUID> {
}
