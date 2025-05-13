package com.assessmint.be.bank.services;

import com.assessmint.be.assessment.helpers.QuestionType;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.bank.dtos.BankDTO;
import com.assessmint.be.bank.dtos.CreateBankDTO;
import com.assessmint.be.bank.dtos.SBankDTO;
import com.assessmint.be.bank.dtos.questions.BankQuestionDTO;
import com.assessmint.be.bank.dtos.questions.add.AddBankMCQDTO;
import com.assessmint.be.bank.dtos.questions.add.AddBankQuestionDTO;
import com.assessmint.be.bank.dtos.questions.add.AddBankTFDTO;
import com.assessmint.be.bank.entities.Bank;
import com.assessmint.be.bank.entities.BankCategory;
import com.assessmint.be.bank.entities.DifficultyLevel;
import com.assessmint.be.bank.entities.questions.BankMCQAnswer;
import com.assessmint.be.bank.entities.questions.BankMultipleChoiceQuestion;
import com.assessmint.be.bank.entities.questions.BankTrueFalseQuestion;
import com.assessmint.be.bank.repositories.BankCategoryRepository;
import com.assessmint.be.bank.repositories.BankRepository;
import com.assessmint.be.bank.repositories.questions.BankMCQAnswerRepository;
import com.assessmint.be.bank.repositories.questions.BankMCQRepository;
import com.assessmint.be.bank.repositories.questions.BankTFRepository;
import com.assessmint.be.global.Utils;
import com.assessmint.be.global.exceptions.ConflictException;
import com.assessmint.be.global.exceptions.NotAuthorizedException;
import com.assessmint.be.global.exceptions.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    private final BankCategoryRepository bankCategoryRepository;
    private final BankTFRepository bankTFRepository;
    private final BankMCQRepository bankMCQRepository;
    private final BankMCQAnswerRepository bankMCQAnswerRepository;

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

    public BankQuestionDTO addQuestion(@Valid AddBankQuestionDTO reqDto, AuthUser user) {
        final Bank bank = bankRepository.findById(UUID.fromString(reqDto.getBankId()))
                .orElseThrow(() -> new NotFoundException("BANK_NOT_FOUND"));

        if (!bank.getOwner().getId().equals(user.getId())) {
            throw new NotAuthorizedException("BANK_ACCESS_NOT_ALLOWED");
        }

        if (bank.getQuestionType() != reqDto.getQuestionType()) {
            throw new ConflictException("QUESTION_TYPE_MISMATCH");
        }

        return switch (reqDto.getQuestionType()) {
            case TRUE_OR_FALSE -> handleAddBankTF(bank, (AddBankTFDTO) reqDto);
            case MULTIPLE_CHOICE -> handleAddBankMCQ(bank, (AddBankMCQDTO) reqDto);
            default -> throw new ConflictException("QUESTION_TYPE_NOT_SUPPORTED");
        };
    }

    private BankQuestionDTO handleAddBankTF(Bank bank, AddBankTFDTO reqDto) {
        final BankTrueFalseQuestion temp = BankTrueFalseQuestion.builder()
                .questionText(reqDto.getQuestionText().trim())
                .answer(Utils.parseBoolean(reqDto.answer))
                .build();

        final BankTrueFalseQuestion saved = bankTFRepository.save(temp);

        bank.addQuestion(saved);
        bankRepository.save(bank);

        return BankQuestionDTO.fromEntity(saved);
    }

    private BankQuestionDTO handleAddBankMCQ(Bank bank, AddBankMCQDTO reqDto) {
        final List<String> options = reqDto.getOptions().stream().map(String::trim).distinct().toList();
        final List<String> answers = reqDto.getAnswers().stream().map(String::trim).distinct().toList();

        final String questionText = reqDto.getQuestionText().trim();

        final List<BankMCQAnswer> answerOptions = options.stream()
                .map(ans -> BankMCQAnswer.builder()
                        .answerText(ans)
                        .build())
                .toList();

        final List<BankMCQAnswer> savedAnswerOptions = bankMCQAnswerRepository.saveAll(answerOptions);

        final List<UUID> answerIds = savedAnswerOptions.stream()
                .filter(ans -> answers.contains(ans.getAnswerText()))
                .map(BankMCQAnswer::getId)
                .toList();

        final BankMultipleChoiceQuestion temp = BankMultipleChoiceQuestion.builder()
                .questionText(questionText)
                .options(savedAnswerOptions)
                .answers(answerIds)
                .build();

        final BankMultipleChoiceQuestion saved = bankMCQRepository.save(temp);

        bank.addQuestion(saved);
        bankRepository.save(bank);

        return BankQuestionDTO.fromEntity(saved);
    }

    public SBankDTO getById(UUID uuid, AuthUser user) {
        final Bank bank = bankRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("BANK_NOT_FOUND"));

        if (!bank.getOwner().getId().equals(user.getId())) {
            throw new NotAuthorizedException("BANK_ACCESS_NOT_ALLOWED");
        }

        return SBankDTO.fromEntity(bank);
    }
}
