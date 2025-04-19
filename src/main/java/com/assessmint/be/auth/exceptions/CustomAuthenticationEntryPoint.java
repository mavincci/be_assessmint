package com.assessmint.be.auth.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
   private final HandlerExceptionResolver exceptionResolver;

   public CustomAuthenticationEntryPoint(
         @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
      this.exceptionResolver = exceptionResolver;
   }

   @Override
   public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
      exceptionResolver.resolveException(request, response, null, authException);
   }
}
