package com.assessmint.be.bank.services;

import com.assessmint.be.bank.dtos.bank_category.BankCategoryDTO;
import com.assessmint.be.bank.dtos.bank_category.CreateBankCategoryDTO;
import com.assessmint.be.bank.entities.BankCategory;
import com.assessmint.be.bank.repositories.BankCategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
