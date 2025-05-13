package com.assessmint.be.bank.repositories;

import com.assessmint.be.bank.entities.BankCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankCategoryRepository extends JpaRepository<BankCategory, UUID> {
}
