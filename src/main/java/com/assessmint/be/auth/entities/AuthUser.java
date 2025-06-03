package com.assessmint.be.auth.entities;

import com.assessmint.be.auth.entities.helpers.AuthRole;
import com.assessmint.be.global.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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
    private String lastName;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<AuthRole> roles = new HashSet<>(Collections.singleton(AuthRole.USER));

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault(value = "true")
    private boolean isActive = true;

    @OneToMany
    private List<PasswordToken> passwordTokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.value))
                .toList();
    }

    public void addRole(AuthRole role) {
        roles.add(role);
    }

    public void removeRole(AuthRole role) {
        roles.remove(role);
    }

    public boolean hasRole(AuthRole role) {
        return roles.contains(role);
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
        return isActive;
    }
}
