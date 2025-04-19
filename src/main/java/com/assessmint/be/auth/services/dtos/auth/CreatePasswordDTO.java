package com.assessmint.be.auth.services.dtos.auth;

import jakarta.validation.constraints.NotEmpty;

public record CreatePasswordDTO(
      @NotEmpty(message = "Password is required")
      String password
) {
}
