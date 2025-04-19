package com.assessmint.be.auth.controllers;

import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.services.AuthUserService;
import com.assessmint.be.auth.services.dtos.authUser.AuthUserDTO;
import com.assessmint.be.global.controllers.dtos.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth/users")
@Tags(value = {
      @Tag(name = "Auth User", description = "Auth User API")
})
@RequiredArgsConstructor
public class AppAuthUserController {
   static final Logger logger = LoggerFactory.getLogger(AppAuthUserController.class);

   final AuthUserService authUserService;

   @GetMapping(value = "/get_all_users", produces = "application/json")
   public ResponseEntity<APIResponse<List<AuthUserDTO>>> getAllUsers() {
      return APIResponse.build(
            HttpStatus.OK.value(),
            "ALL_USERS-FETCH_SUCCESS",
            authUserService.getAllUsers()
      );
   }

   @GetMapping(value = "/who_am_i", produces = "application/json")
   public ResponseEntity<APIResponse<AuthUserDTO>> whoAmI(
         @AuthenticationPrincipal AuthUser user) {
      return APIResponse.build(
            HttpStatus.OK.value(),
            "YOU_ARE",
            AuthUserDTO.fromEntity(user)
      );
   }
}
