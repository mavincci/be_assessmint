package com.assessmint.be.dashboard.services;

import com.assessmint.be.assessment.entities.Assessment;
import com.assessmint.be.assessment.repositories.AssessmentRepository;
import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.entities.helpers.AuthRole;
import com.assessmint.be.auth.repositories.AuthUserRepository;
import com.assessmint.be.bank.entities.Bank;
import com.assessmint.be.bank.repositories.BankRepository;
import com.assessmint.be.dashboard.dtos.AdminDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final AuthUserRepository authUserRepository;
    private final BankRepository bankRepository;
    private final AssessmentRepository assessmentRepository;

    public AdminDashboardDTO getAdminDashboard() {
        final List<AuthUser> allUsers = authUserRepository.findAll();
        final List<AuthUser> examiners = allUsers.stream()
                .filter(u -> u.hasRole(AuthRole.EXAMINER))
                .toList();

        final List<Bank> allBanks = bankRepository.findAll();
        final List<Assessment> allAssessments = assessmentRepository.findAll();

        return new AdminDashboardDTO(
                allUsers.size(),
                examiners.size(),
                allBanks.size(),
                allAssessments.size()
        );
    }
}
