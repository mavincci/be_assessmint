package com.assessmint.be.global.controllers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Constraint(validatedBy = UUIDValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
   String message() default "Invalid UUID";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};
}

class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

   @Override
   public void initialize(ValidUUID constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
   }

   @Override
   public boolean isValid(String uuid, ConstraintValidatorContext context) {
      if (uuid == null || uuid.trim().isEmpty()) {
         return true; // Use @NotNull or @NotEmpty for null/empty check
      }

      try {
         UUID.fromString(uuid);
         return true;
      } catch (IllegalArgumentException e) {
         return false;
      }
   }
}
