package com.assessmint.be.bank.dtos.bank_category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CreateBankCategoryDTO(
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        String description
) {
}
