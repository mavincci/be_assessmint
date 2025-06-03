package com.assessmint.be.auth.configurations;

import com.assessmint.be.auth.services.AuthUserDetailService;
import com.assessmint.be.auth.services.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthUserDetailService authUserDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final var reqPath = request.getRequestURI();

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final var jwtToken = authHeader.substring(7);

        String userEmail;

        try {
            userEmail = jwtUtils.extractUsername(jwtToken);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"statusCode\": 401, \"message\": \"AUTH_TOKEN_EXPIRED\"}");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        } catch (MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"statusCode\": 401, \"message\": \"AUTH_TOKEN_INVALID\"}");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        if (!userEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            final var userDetails = authUserDetailService.loadUserByUsername(userEmail);

            if (!userDetails.isEnabled()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"statusCode\": 401, \"message\": \"USER_IS_DISABLED\"}");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                final var securityContext = SecurityContextHolder.createEmptyContext();
                final var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authToken);

                SecurityContextHolder.setContext(securityContext);
            }
        }

        filterChain.doFilter(request, response);
    }
}
