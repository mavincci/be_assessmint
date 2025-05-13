package com.assessmint.be.bank.services;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.bank.dtos.BankDTO;
import com.assessmint.be.bank.dtos.CreateBankDTO;
import com.assessmint.be.bank.entities.Bank;
import com.assessmint.be.bank.entities.BankCategory;
import com.assessmint.be.bank.entities.DifficultyLevel;
import com.assessmint.be.bank.repositories.BankCategoryRepository;
import com.assessmint.be.bank.repositories.BankRepository;
import com.assessmint.be.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    private final BankCategoryRepository bankCategoryRepository;

    public BankDTO createBank(CreateBankDTO reqDto, AuthUser user) {
        final BankCategory bankCategory = bankCategoryRepository
                .findById(UUID.fromString(reqDto.categoryId()))
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND"));

        final String name = reqDto.name().trim();
        final String description = reqDto.description().trim();

        final Bank temp = Bank.builder()
                .name(name)
                .description(description)
                .questionType(QuestionType.valueOf(reqDto.questionType()))
                .category(bankCategory)
                .difficultyLevel(DifficultyLevel.fromValue(reqDto.difficultyLevel()))
                .owner(user)
                .build();

        final Bank saved = bankRepository.save(temp);

        return BankDTO.fromEntity(saved);
    }

    public List<BankDTO> mine(AuthUser user) {
        final List<Bank> userBanks = bankRepository.findAllByOwnerId(user.getId());

        return userBanks.stream().map(BankDTO::fromEntity).toList();
    }
}
