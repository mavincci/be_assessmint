package com.assessmint.be.auth.services.dtos.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.assessmint.be.auth.services.dtos.authUser.AuthUserDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponseDTO(
      String token,
      String refreshToken,
      AuthUserDTO user
) {
}
