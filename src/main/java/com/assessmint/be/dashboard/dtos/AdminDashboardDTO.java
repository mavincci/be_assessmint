package com.assessmint.be.dashboard.dtos;

public record AdminDashboardDTO(
        int totalUsers,
        int totalExaminers,
        int totalBanks,
        int totalAssessments
) {
}
