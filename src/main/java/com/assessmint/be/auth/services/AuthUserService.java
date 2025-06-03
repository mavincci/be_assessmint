package com.assessmint.be.auth.services;

import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.repositories.AuthUserRepository;
import com.assessmint.be.auth.services.dtos.auth.ToggleActiveDTO;
import com.assessmint.be.auth.services.dtos.authUser.AuthUserDTO;
import com.assessmint.be.global.exceptions.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    static final Logger logger = LoggerFactory.getLogger(AuthUserService.class);

    private final AuthUserRepository authUserRepository;

    public AuthUser _getAuthUserByIdNotDeleted(UUID id) {
        return authUserRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("AUTH_USER_NOT_FOUND"));
    }

    public AuthUser _getAnyUserByEmail(String email) {
        return authUserRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Auth user not found"));
    }

    public AuthUser _getAuthUserByEmailNotDeleted(String email) {
        return authUserRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new NotFoundException("Auth user not found"));
    }

    public List<AuthUser> _getAllAuthUsersNotDeleted() {
        return authUserRepository.findAllByIsDeletedFalse();
    }

    public List<AuthUserDTO> getAllUsers() {
        logger.info("Fetching all users");

        final var users = _getAllAuthUsersNotDeleted();

        return users
                .stream()
                .map(AuthUserDTO::fromEntity)
                .toList();
    }

    public AuthUserDTO getUserById(UUID id) {
        logger.info("Fetching user with id: {}", id);

        final var user = _getAuthUserByIdNotDeleted(id);

        return AuthUserDTO.fromEntity(user);
    }

    public AuthUserDTO getUserByEmail(String email) {
        logger.info("Fetching user with email: {}", email);

        final var user = _getAuthUserByEmailNotDeleted(email);

        return AuthUserDTO.fromEntity(user);
    }

    public AuthUserDTO toggleActive(@Valid ToggleActiveDTO reqdto) {
        logger.info("Toggling active status of user with id: {}", reqdto.userId());

        final var user = _getAuthUserByIdNotDeleted(
                UUID.fromString(reqdto.userId()));

        user.setActive(!user.isActive());

        authUserRepository.save(user);

        return AuthUserDTO.fromEntity(user);
    }
}
