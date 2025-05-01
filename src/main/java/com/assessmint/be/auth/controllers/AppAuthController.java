package com.assessmint.be.auth.controllers;

import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.services.AuthService;
import com.assessmint.be.auth.services.AuthUserService;
import com.assessmint.be.auth.services.dtos.auth.*;
import com.assessmint.be.auth.services.dtos.authUser.AuthUserDTO;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import com.assessmint.be.global.controllers.validators.ValidUUID;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Tags(value = {
        @Tag(name = "Auth", description = "Auth API")
})
@RequiredArgsConstructor
public class AppAuthController {

    private final AuthService authService;
    private final AuthUserService authUserService;

//    @PostMapping("/signup")
//    public ResponseEntity<APIResponse<AuthUserDTO>> register(
//            @Valid @RequestBody RegisterRequestDTO request) {
//        return APIResponse.build(
//                201,
//                "USER_SIGNUP_SUCCESS",
//                authService.register(request)
//        );
//    }

    @PostMapping("/signup_as_examinee")
    public ResponseEntity<APIResponse<AuthUserDTO>> signupAsExaminee(
            @Valid @RequestBody RegisterRequestDTO request) {
        return APIResponse.build(
                201,
                "EXAMINEE_SIGNUP_SUCCESS",
                authService.signupAsExaminee(request)
        );
    }

    @PostMapping("/signup_as_examiner")
    public ResponseEntity<APIResponse<AuthUserDTO>> signupAsExaminer(
            @Valid @RequestBody RegisterRequestDTO request) {
        return APIResponse.build(
                201,
                "EXAMINER_SIGNUP_SUCCESS",
                authService.signupAsExaminer(request)
        );
    }

    @PostMapping("/signup_admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<AuthUserDTO>> registerAdmin(
            @Valid @RequestBody RegisterRequestDTO request) {
        return APIResponse.build(
                201,
                "ADMIN_SIGNUP_SUCCESS",
                authService.registerAdmin(request)
        );
    }

    @PostMapping("/signin")
    public ResponseEntity<APIResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        return APIResponse.build(
                200,
                "USER_SIGNIN_SUCCESS",
                authService.login(request)
        );
    }

    @PostMapping("/create_password/{password_token}")
    public ResponseEntity<APIResponse<Object>> createPassword(
            @Validated @ValidUUID @PathVariable("password_token") String passwordTokenId,
            @Valid @RequestBody CreatePasswordDTO request) {
        return APIResponse.build(
                HttpStatus.CREATED.value(),
                "PASSWORD_CREATE__SUCCESS",
                authService.createPassword(
                        UUID.fromString(passwordTokenId),
                        request
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<LoginResponseDTO>> refreshToken(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestHeader("Authorization") String authzn
    ) {
        final var jwtToken = authzn.substring(7);

        return APIResponse.build(
                200,
                "AUTH_TOKEN_REFRESH_SUCCESS",
                authService.refreshToken(authUser, jwtToken)
        );
    }

    @PostMapping("/change_password")
    public ResponseEntity<APIResponse<Object>> changePassword(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody ChangePasswordRequestDTO request) {
        authService.changePassword(user, request);
        return APIResponse.build(
                HttpStatus.OK.value(),
                "PASSWORD_CHANGE_SUCCESS",
                null
        );
    }
}
