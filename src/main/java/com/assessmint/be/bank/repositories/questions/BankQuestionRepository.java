package com.assessmint.be.bank.repositories.questions;

import com.assessmint.be.bank.entities.questions.BankQuestion;
import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankQuestionRepository extends JpaRepository<BankQuestion, UUID> {
}
