package com.assessmint.be.global;

import com.assessmint.be.auth.entities.AuthUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

   @Override
   public Optional<String> getCurrentAuditor() {
      final var authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null)
         return Optional.of("seedr");

      final var principal = authentication.getPrincipal();

      if (principal instanceof String)
         return Optional.of("anon");

      return Optional.of(((AuthUser) principal).getEmail());
   }
}
