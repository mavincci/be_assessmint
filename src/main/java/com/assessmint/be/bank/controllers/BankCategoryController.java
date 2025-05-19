package com.assessmint.be.bank.controllers;


import com.assessmint.be.bank.dtos.bank_category.BankCategoryDTO;
import com.assessmint.be.bank.dtos.bank_category.CategoryBankDTO;
import com.assessmint.be.bank.dtos.bank_category.CreateBankCategoryDTO;
import com.assessmint.be.bank.services.BankCategoryService;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments/bank/categories")
@RequiredArgsConstructor
public class BankCategoryController {
    private final BankCategoryService bankCategoryService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<BankCategoryDTO>> createBankCategory(
            @Valid @RequestBody
            CreateBankCategoryDTO reqdto
    ) {
        return APIResponse.build(
                HttpStatus.CREATED.value(),
                "BANK_CATEGORY_CREATE_SUCCESS",
                bankCategoryService.createBankCategory(reqdto)
        );
    }

    @GetMapping("/get_all")
    public ResponseEntity<APIResponse<List<BankCategoryDTO>>> getAllBankCategories() {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "BANK_CATEGORY_GET_ALL_SUCCESS",
                bankCategoryService.getAll()
        );
    }

    @GetMapping("/get_banks/{categoryId}")
    public ResponseEntity<APIResponse<List<CategoryBankDTO>>> getBanksByCategoryId(
            @Validated @NotBlank @UUID
            @PathVariable("categoryId") String categoryId) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "BANK_CATEGORY_GET_ALL_SUCCESS",
                bankCategoryService.getBanksByCategory(
                        java.util.UUID.fromString(categoryId))
        );
    }
}
