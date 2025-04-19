package com.assessmint.be.global.controllers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URL;

@Constraint(validatedBy = UrlValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidURL {
   String message() default "Invalid URL";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};
}

class UrlValidator implements ConstraintValidator<ValidURL, String> {
   @Override
   public void initialize(ValidURL constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
   }

   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null || value.isEmpty())
         return true;

      try {
         new URL(value);
         return true;
      } catch (MalformedURLException e) {
         return false;
      }
   }
}
