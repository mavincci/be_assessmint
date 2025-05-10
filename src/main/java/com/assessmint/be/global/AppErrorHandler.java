package com.assessmint.be.global;

import com.assessmint.be.global.controllers.dtos.APIResponse;
import com.assessmint.be.global.exceptions.ConflictException;
import com.assessmint.be.global.exceptions.EnumIllegalArgumentException;
import com.assessmint.be.global.exceptions.NotAuthorizedException;
import com.assessmint.be.global.exceptions.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AppErrorHandler {
   static final Logger logger = LoggerFactory.getLogger(AppErrorHandler.class);

   @ExceptionHandler(value = {ExpiredJwtException.class})
   public ResponseEntity<APIResponse<Object>> handleExpiredJwtException(ExpiredJwtException e) {
      throw new NotAuthorizedException("TOKEN_IS_EXPIRED");
   }

   @ExceptionHandler(value = BadCredentialsException.class)
   public ResponseEntity<APIResponse<Object>> handleBadCredentialsException(BadCredentialsException e) {
      return APIResponse.build(
            HttpStatus.UNAUTHORIZED.value(),
            e.getMessage(),
            null
      );
   }

   @ExceptionHandler(value = AuthorizationDeniedException.class)
   public ResponseEntity<APIResponse<Object>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
      return APIResponse.build(
            HttpStatus.UNAUTHORIZED.value(),
            e.getMessage(),
            null
      );
   }

   @ExceptionHandler(value = {NotAuthorizedException.class})
   public ResponseEntity<APIResponse<Object>> handleNotAuthorizedException(NotAuthorizedException e) {
      return APIResponse.build(
            HttpStatus.UNAUTHORIZED.value(),
            e.getMessage(),
            null
      );
   }

   @ExceptionHandler(value = {InsufficientAuthenticationException.class})
   public ResponseEntity<APIResponse<Object>> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
      return APIResponse.build(
            HttpStatus.FORBIDDEN.value(),
            "NOT_AUTHORIZED",
            null
      );
   }

   @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
   public ResponseEntity<APIResponse<Object>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            "Media type " + e.getContentType() + " not allowed",
            null
      );
   }

   @ExceptionHandler(value = {HttpMessageNotReadableException.class})
   public ResponseEntity<APIResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
      e.printStackTrace();
      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            "REQUIRED_REQUEST_BODY_MISSING",
            null
      );
   }

   @ExceptionHandler(value = {NotFoundException.class})
   public ResponseEntity<APIResponse<Object>> handleNotFoundException(NotFoundException e) {
      return APIResponse.build(
            HttpStatus.NOT_FOUND.value(),
            e.getMessage(),
            null
      );
   }

   @ExceptionHandler(value = {ConflictException.class})
   public ResponseEntity<APIResponse<Object>> handleConflictException(ConflictException e) {
      return APIResponse.build(
            HttpStatus.CONFLICT.value(),
            e.getMessage(),
            null
      );
   }

   @ExceptionHandler(value = {IllegalArgumentException.class})
   public ResponseEntity<APIResponse<Object>> handleNotFoundException(IllegalArgumentException e) {
      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            null
      );
   }

   @ExceptionHandler(value = {EnumIllegalArgumentException.class})
   public ResponseEntity<APIResponse<Object>> handleNotFoundException(EnumIllegalArgumentException e) {
      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            null
      );
   }

   @ExceptionHandler(value = {NoResourceFoundException.class})
   public ResponseEntity<APIResponse<Object>> handleNoResourceFoundException(NoResourceFoundException e) {
      logger.info("Unknown endpoint requested {}", e.getResourcePath());
      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            "Endpoint " + e.getResourcePath() + " is not found.",
            null
      );
   }

   @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
   public ResponseEntity<APIResponse<Object>> handleHttpRequestMethodNotSupportedException(
         HttpRequestMethodNotSupportedException e) {
      logger.info("Unknown method requested {}", e.getMethod());
      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            "Method " + e.getMethod() + " is not found on this endpoint.",
            null
      );
   }

   //   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<APIResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {

      Map<String, String> errors = new HashMap<>();

      ex.getBindingResult().getAllErrors().forEach((error) -> {
         if (error instanceof FieldError) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
         } else {
            String fieldName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
         }
      });

      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            errors
      );
   }

   @ExceptionHandler(HandlerMethodValidationException.class)
   public ResponseEntity<APIResponse<Map<String, String>>> handleMethodValidationExceptions(HandlerMethodValidationException ex) {

      Map<String, String> errors = new HashMap<>();

      ex.getAllValidationResults().forEach(result -> {
         errors.put(
               result.getMethodParameter().getParameterName(),
               result.getResolvableErrors().get(result.getMethodParameter().getParameterIndex()).getDefaultMessage()
         );
      });

      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            errors
      );
   }

   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   public ResponseEntity<APIResponse<Map<String, String>>> handleMethodValidationExceptions(MethodArgumentTypeMismatchException ex) {

      Map<String, String> errors = new HashMap<>();

      errors.put(
            ex.getParameter().getParameterName(),
            String.format("the required type is - %s", ex.getRequiredType().getSimpleName().toLowerCase()));

      return APIResponse.build(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            errors
      );
   }

   @ExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public ResponseEntity<APIResponse<Object>> handleAllExceptions(Exception e) {
      e.printStackTrace();
      return APIResponse.build(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred while processing your request" + e.getClass().getName(),
            null
      );
   }
}
