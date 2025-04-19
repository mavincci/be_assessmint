package com.assessmint.be.auth.services;

import com.assessmint.be.auth.entities.AuthUser;
import com.assessmint.be.auth.entities.PasswordToken;
import com.assessmint.be.auth.entities.helpers.AuthRole;
import com.assessmint.be.auth.repositories.AuthUserRepository;
import com.assessmint.be.auth.repositories.PasswordTokenRepository;
import com.assessmint.be.auth.services.dtos.auth.*;
import com.assessmint.be.auth.services.dtos.authUser.AuthUserDTO;
import com.assessmint.be.global.exceptions.ConflictException;
import com.assessmint.be.global.exceptions.NotAuthorizedException;
import com.assessmint.be.global.exceptions.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

   private final AuthUserService authUserService;
   private final AuthUserRepository authUserRepository;

   private final PasswordTokenRepository passwordTokenRepository;

   private final JwtUtils jwtUtils;
   private final PasswordEncoder passwordEncoder;

   private final AuthenticationManager authenticationManager;

   public AuthUser _registerAuthUserNoPwd(RegisterRequestNoPwdDTO requestDto, AuthRole authRole) {
      try {
         authUserService._getAnyUserByEmail(requestDto.email.toLowerCase());
         throw new ConflictException("EMAIL_ALREADY_EXISTS");
      } catch (NotFoundException ignored) {
      }

      final var tempUser = new AuthUser(
            requestDto.email.toLowerCase(),
            "",
            requestDto.firstName,
            requestDto.middleName,
            requestDto.lastName,
            authRole
      );

      return authUserRepository.save(tempUser);
   }

   public AuthUser _registerAuthUser(RegisterRequestDTO requestDto, AuthRole authRole) {
      try {
         authUserService._getAnyUserByEmail(requestDto.email.toLowerCase());
         throw new ConflictException("EMAIL_ALREADY_EXISTS");
      } catch (NotFoundException ignored) {
      }

      final var tempUser = new AuthUser(
            requestDto.email.trim().toLowerCase(),
            passwordEncoder.encode(requestDto.password),
            requestDto.firstName.trim().toLowerCase(),
            requestDto.middleName.trim().toLowerCase(),
            requestDto.lastName.trim().toLowerCase(),
            authRole
      );

      return authUserRepository.save(tempUser);
   }

   public AuthUserDTO register(RegisterRequestDTO requestDto) {

      final var saved = _registerAuthUser(requestDto, AuthRole.USER);

      return AuthUserDTO.fromEntity(saved);
   }

   public AuthUserDTO registerAdmin(@Valid RegisterRequestDTO requestDto) {
      final var saved = _registerAuthUser(requestDto, AuthRole.ADMIN);

      return AuthUserDTO.fromEntity(saved);
   }

   public LoginResponseDTO login(LoginRequestDTO requestDto) {
      authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                  requestDto.email().toLowerCase(), requestDto.password()));

      final var authUser = authUserService._getAuthUserByEmailNotDeleted(requestDto.email());

      final var token = jwtUtils.generateToken(authUser);
      final var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), authUser);

      return new LoginResponseDTO(
            token,
            refreshToken,
            AuthUserDTO.fromEntity(authUser)
      );
   }

   public LoginResponseDTO refreshToken(AuthUser authUser, String token) {
      final var email = jwtUtils.extractUsername(token);
      final var _authUser = authUserService._getAuthUserByEmailNotDeleted(email);

      if (!authUser.getId().equals(_authUser.getId())) {
         throw new NotAuthorizedException("INVALID_AUTH_TOKEN");
      }

      if (jwtUtils.isTokenValid(token, authUser)) {
         final var t = jwtUtils.generateToken(authUser);
         return new LoginResponseDTO(
               t,
               token,
               AuthUserDTO.fromEntity(authUser)
         );
      }
      throw new NotAuthorizedException("INVALID_AUTH_TOKEN");
   }

   public void changePassword(AuthUser authUser, ChangePasswordRequestDTO requestDto) {

      if (passwordEncoder.matches(requestDto.oldPassword, authUser.getPassword())) {
         authUser.setPassword(passwordEncoder.encode(requestDto.newPassword));
         authUserRepository.save(authUser);
         return;
      }
      throw new ConflictException("INVALID_OLD_PASSWORD");
   }

   public PasswordToken _createPasswordToken(AuthUser authUser) {
      final var expiresAt = LocalDateTime.now().plusDays(3);

      final var pwdToken = new PasswordToken(authUser, expiresAt);

      return passwordTokenRepository.save(pwdToken);
   }

   public AuthUserDTO createPassword(UUID passwordToken, CreatePasswordDTO request) {
      final var pwdToken = passwordTokenRepository.findById(passwordToken)
            .orElseThrow(() -> new NotFoundException("Password token not found"));

      final var now = LocalDateTime.now();

      if (pwdToken.getIsRedeemed())
         throw new ConflictException("Password token has already been used");

      if (pwdToken.getExpiresAt().isBefore(now))
         throw new ConflictException("Password token has expired");

      final var pwd = passwordEncoder.encode(request.password());

      final var usr = pwdToken.getAuthUser();

      usr.setPassword(pwd);

      pwdToken.setIsRedeemed(true);
      pwdToken.setRedeemedAt(now);
      pwdToken.setAuthUser(usr);

      final var saved = passwordTokenRepository.save(pwdToken);

      return AuthUserDTO.fromEntity(saved.getAuthUser());
   }

}
