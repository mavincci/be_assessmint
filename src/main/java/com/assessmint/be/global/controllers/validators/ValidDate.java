package com.assessmint.be.global.controllers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
   String message() default "Invalid date format (\"yyyy-MM-dd\")";

   String pattern() default "yyyy-MM-dd";  // Default pattern

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};
}

class DateValidator implements ConstraintValidator<ValidDate, String> {

   private String pattern;

   @Override
   public void initialize(ValidDate constraintAnnotation) {
      this.pattern = constraintAnnotation.pattern();
   }

   @Override
   public boolean isValid(String date, ConstraintValidatorContext context) {
      if (date == null || date.trim().isEmpty()) {
         return true;  // Use @NotNull or @NotEmpty for null/empty check
      }

      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      sdf.setLenient(false);
      try {
         sdf.parse(date);
         return true;
      } catch (ParseException e) {
         return false;
      }
   }
}
