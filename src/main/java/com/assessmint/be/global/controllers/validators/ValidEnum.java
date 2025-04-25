package com.assessmint.be.global.controllers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
   String message() default "VALUE_NOT_ALLOWED";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};

   Class<? extends Enum<?>> enumClass();
}

class EnumValidator implements ConstraintValidator<ValidEnum, String> {

   private Class<? extends Enum<?>> enumClass;

   @Override
   public void initialize(ValidEnum constraintAnnotation) {
      this.enumClass = constraintAnnotation.enumClass();
   }

   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
         return true;
      }

      Object[] enumValues = this.enumClass.getEnumConstants();
      for (Object enumValue : enumValues) {
         if (value.equals(enumValue.toString())) {
            return true;
         }
      }

      return false;
   }
}
