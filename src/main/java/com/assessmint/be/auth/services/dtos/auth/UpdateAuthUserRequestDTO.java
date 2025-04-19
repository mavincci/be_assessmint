package com.assessmint.be.auth.services.dtos.auth;

import com.assessmint.be.auth.entities.AuthUser;
import jakarta.validation.constraints.Size;

public record UpdateAuthUserRequestDTO(
      @Size(min = 2, max = 20, message = "First name is required to be between 2 and 20 characters")
      String firstName,

      @Size(min = 2, max = 20, message = "Middle name is required to be between 2 and 20 characters")
      String middleName,

      @Size(min = 2, max = 20, message = "last name is required to be between 2 and 20 characters")
      String lastName
) {
   public AuthUser updateEntity(AuthUser entity) {
      if (this.firstName() != null) entity.setFirstName(this.firstName());
      if (this.middleName() != null) entity.setMiddleName(this.middleName());
      if (this.lastName() != null) entity.setLastName(this.lastName());
      return entity;
   }
}
