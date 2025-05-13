package com.assessmint.be.bank.repositories.questions;

import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankTFRepository extends JpaRepository<BankTrueFalseQuestion, UUID> {
}
