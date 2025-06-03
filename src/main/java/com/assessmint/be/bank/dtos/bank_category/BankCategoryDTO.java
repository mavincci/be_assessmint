package com.assessmint.be.bank.dtos.bank_category;

import com.assessmint.be.bank.entities.BankCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public record BankCategoryDTO(
        UUID id,
        String name,
        String description,
        int bankCount,
        LocalDateTime createdAt
) {
    public static BankCategoryDTO fromEntity(BankCategory entity) {
        return new BankCategoryDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getBanks().size(),
                entity.getCreatedAt()
        );
    }
}
