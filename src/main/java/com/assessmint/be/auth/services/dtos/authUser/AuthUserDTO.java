package com.assessmint.be.auth.services.dtos.authUser;

import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.entities.helpers.AuthRole;

import java.util.List;
import java.util.UUID;

public record AuthUserDTO(
        UUID id,
        String email,
        String firstName,
        String lastName,
        List<AuthRole> role
) {
    public static AuthUserDTO fromEntity(AuthUser authUser) {
        return new AuthUserDTO(
                authUser.getId(),
                authUser.getEmail(),
                authUser.getFirstName(),
                authUser.getLastName(),
                authUser.getRoles().stream().toList()
        );
    }
}
