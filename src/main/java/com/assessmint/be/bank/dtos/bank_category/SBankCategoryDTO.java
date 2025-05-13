package com.assessmint.be.bank.dtos.bank_category;

import com.assessmint.be.bank.entities.BankCategory;

import java.util.UUID;

public record SBankCategoryDTO(
        UUID id,
        String name
) {
    public static SBankCategoryDTO fromEntity(BankCategory entity) {
        return new SBankCategoryDTO(
                entity.getId(),
                entity.getName()
        );
    }
}
