package com.assessmint.be.dashboard.controllers;


import com.assessmint.be.dashboard.dtos.AdminDashboardDTO;
import com.assessmint.be.dashboard.services.DashboardService;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Tags(value = {
        @Tag(name = "Dashboard", description = "Dashboard API")
})
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<AdminDashboardDTO>> getAdminDashboard() {
        return APIResponse.build(
                HttpStatus.OK.value(),
                "ADMIN_DASHBOARD_FETCH_SUCCESS",
                dashboardService.getAdminDashboard()
        );
    }
}
