package com.assessmint.be.auth.services.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
      @Email(message = "Invalid email address")
      @NotEmpty(message = "Email is required")
      String email,

      @NotEmpty(message = "Password is required")
      String password
) {
}
