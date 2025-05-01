package com.assessmint.be.auth.configurations;

import com.assessmint.be.auth.exceptions.CustomAccessDeniedHandler;
import com.assessmint.be.auth.exceptions.CustomAuthenticationEntryPoint;
import com.assessmint.be.auth.services.AuthUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class SecurityConfiguration {

   private final AuthUserDetailService authUserDetailService;
   private final JwtAuthFilter jwtAuthFilter;

   private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
   private final CustomAccessDeniedHandler customAccessDeniedHandler;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
      security
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(r ->
                  r
                        // Auth paths
                        .requestMatchers(
                              "/auth/signup",
                              "/auth/signin",
                              "/auth/signup_as_examiner",
                              "/auth/signup_as_examinee",
                              "/auth/create_password/*"
                        ).permitAll()
                        .requestMatchers("/api/auth/*").authenticated()

                        // Documentation paths
                        .requestMatchers("/api-doc/*", "/api-doc*", "/swagger-ui/*", "/error").permitAll()

                        .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(
                  jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
            )
            .exceptionHandling(e ->
                  e
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
            )
            .formLogin(AbstractHttpConfigurer::disable);

      return security.build();
   }

   @Bean
   public AuthenticationProvider authenticationProvider() {
      final var daoAuthProvider = new DaoAuthenticationProvider();

      daoAuthProvider.setUserDetailsService(authUserDetailService);
      daoAuthProvider.setPasswordEncoder(passwordEncoder());

      return daoAuthProvider;
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(
         AuthenticationConfiguration authenticationConfiguration)
         throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of("*"));
      configuration.setAllowedMethods(List.of("*"));
      configuration.setAllowedHeaders(List.of("*"));
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
   }
}
