package com.assessmint.be.auth.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
   private static final Logger logger = LoggerFactory.getLogger("Logger");

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {

      logger.info(
            "Req: {}\t{}\t\t <--- {}:{}",
            request.getMethod(),
            request.getRequestURI(),
            request.getRemoteAddr(),
            request.getRemotePort());

      filterChain.doFilter(request, response);

      logger.info(
            "Res: {} {}\t{}\t\t ---> {}:{}",
            response.getStatus(),
            request.getMethod(),
            request.getRequestURI(),
            request.getRemoteAddr(),
            request.getRemotePort());
   }
}
