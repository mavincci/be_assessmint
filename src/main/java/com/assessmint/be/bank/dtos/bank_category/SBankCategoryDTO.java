package com.assessmint.be.bank.dtos.bank_category;

import com.assessmint.be.bank.entities.BankCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public record SBankCategoryDTO(
        UUID id,
        String name,
        LocalDateTime createdAt
) {
    public static SBankCategoryDTO fromEntity(BankCategory entity) {
        return new SBankCategoryDTO(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt()
        );
    }
}
