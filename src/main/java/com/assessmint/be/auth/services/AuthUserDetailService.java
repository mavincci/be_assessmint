package com.assessmint.be.auth.services;

import com.assessmint.be.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {
   private final AuthUserService authUserService;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      try {
         System.out.println("Here loaded the user");
         return authUserService._getAuthUserByEmailNotDeleted(username);
      } catch (NotFoundException e) {
         throw new UsernameNotFoundException("Auth User not found");
      }
   }
}
