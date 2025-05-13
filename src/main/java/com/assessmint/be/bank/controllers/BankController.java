package com.assessmint.be.bank.controllers;

import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.bank.dtos.BankDTO;
import com.assessmint.be.bank.dtos.CreateBankDTO;
import com.assessmint.be.bank.services.BankService;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments/bank")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<BankDTO>> createBank(
            @Valid @RequestBody CreateBankDTO reqDto,
            @AuthenticationPrincipal AuthUser user
    ) {
        return APIResponse.build(
                HttpStatus.CREATED.value(),
                "QUESTION_BANK_CREATE_SUCCESS",
                bankService.createBank(reqDto, user)
        );
    }

    @GetMapping("/mine")
    public ResponseEntity<APIResponse<List<BankDTO>>> getMyBanks(
            @AuthenticationPrincipal AuthUser user) {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "QUESTION_BANK_FETCH_SUCCESS",
                bankService.mine(user)
        );
    }
}
