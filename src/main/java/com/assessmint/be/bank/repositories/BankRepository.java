package com.assessmint.be.bank.repositories;

import com.assessmint.be.bank.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
    List<Bank> findAllByOwnerId(UUID ownerId);
}
