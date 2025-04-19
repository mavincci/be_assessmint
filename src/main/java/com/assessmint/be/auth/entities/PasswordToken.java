package com.assessmint.be.auth.entities;


import com.assessmint.be.global.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "auth_password_token")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordToken extends Auditable {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;

   @ManyToOne
   private AuthUser authUser;

   @Column
   private LocalDateTime expiresAt;

   @Builder.Default
   @Column(nullable = false)
   private Boolean isRedeemed = false;

   @Column
   private LocalDateTime redeemedAt;

   public PasswordToken(AuthUser authUser, LocalDateTime expiresAt) {
      this.authUser = authUser;
      this.expiresAt = expiresAt;
      isRedeemed = false;
   }
}
