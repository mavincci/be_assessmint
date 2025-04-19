package com.assessmint.be.auth.services.dtos.authUser;

import com.assessmint.be.auth.entities.PasswordToken;

import java.time.LocalDateTime;
import java.util.UUID;

public record PasswordTokenDTO(
      UUID id,
      UUID userId,
      LocalDateTime expiresAt
) {
   public static PasswordTokenDTO fromEntity(PasswordToken passwordToken) {
      return new PasswordTokenDTO(
            passwordToken.getId(),
            passwordToken.getAuthUser().getId(),
            passwordToken.getExpiresAt()
      );
   }
}
