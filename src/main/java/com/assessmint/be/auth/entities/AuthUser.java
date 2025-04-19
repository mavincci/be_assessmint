package com.assessmint.be.auth.entities;

import com.assessmint.be.auth.entities.helpers.AuthRole;
import com.assessmint.be.global.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "auth_auth_user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser extends Auditable implements UserDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;

   @Column(nullable = false, unique = true)
   private String email;

   @Column
   private String password;

   @Column
   private String firstName;

   @Column
   private String middleName;

   @Column
   private String lastName;

   @Builder.Default
   @Enumerated(EnumType.STRING)
   private AuthRole role = AuthRole.USER;

   @OneToMany
   private List<PasswordToken> passwordTokens;

   public AuthUser(String email, String password, String firstName, String middleName, String lastName, AuthRole role) {
      this.email = email;
      this.password = password;
      this.firstName = firstName;
      this.middleName = middleName;
      this.lastName = lastName;
      this.role = role;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(
            new SimpleGrantedAuthority("ROLE_" + role.value)
      );
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
