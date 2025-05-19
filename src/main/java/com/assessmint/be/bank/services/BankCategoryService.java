package com.assessmint.be.bank.services;

import com.assessmint.be.bank.dtos.bank_category.BankCategoryDTO;
import com.assessmint.be.bank.dtos.bank_category.CategoryBankDTO;
import com.assessmint.be.bank.dtos.bank_category.CreateBankCategoryDTO;
import com.assessmint.be.bank.entities.BankCategory;
import com.assessmint.be.bank.repositories.BankCategoryRepository;
import com.assessmint.be.global.exceptions.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankCategoryService {
    private final BankCategoryRepository bankCategoryRepository;

    public BankCategoryDTO createBankCategory(@Valid CreateBankCategoryDTO reqdto) {
        final String name = reqdto.name().trim();
        final String description = reqdto.description().trim();

        final BankCategory temp = BankCategory.builder()
                .name(name)
                .description(description)
                .build();

        final BankCategory saved = bankCategoryRepository.save(temp);

        return BankCategoryDTO.fromEntity(saved);
    }

    public List<BankCategoryDTO> getAll() {
        final List<BankCategory> categories = bankCategoryRepository.findAll();
        return categories.stream().map(BankCategoryDTO::fromEntity).toList();
    }

    public List<CategoryBankDTO> getBanksByCategory(UUID categoryId) {
        final BankCategory category = bankCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("BANK_CATEGORY_NOT_FOUND"));

        return category.getBanks().stream()
                .map(CategoryBankDTO::fromEntity)
                .toList();
    }
}
