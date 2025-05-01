package com.assessmint.be.auth.services.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignupDTO {

   @NotNull(message = "First name is required")
   @NotEmpty(message = "First name is required")
   public final String firstName;

   @NotNull(message = "Last name is required")
   @NotEmpty(message = "Last name is required")
   public final String lastName;

   @NotNull(message = "Email is required")
   @NotEmpty(message = "Email is required")
   @Email(message = "Invalid email format")
   public final String email;

   @NotNull(message = "Password is required")
   @NotEmpty(message = "Password is required")
   public final String password;
}
