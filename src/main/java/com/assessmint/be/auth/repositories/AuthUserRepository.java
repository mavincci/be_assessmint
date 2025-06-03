package com.assessmint.be.auth.repositories;

import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.entities.helpers.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByIdAndIsDeletedFalse(UUID id);

    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByEmailAndIsDeletedFalse(String email);

    List<AuthUser> findAllByIsDeletedFalse();

    List<AuthUser> findAllByRolesContaining(AuthRole role);
}
