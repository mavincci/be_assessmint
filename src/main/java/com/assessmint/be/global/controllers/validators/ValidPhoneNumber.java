package com.assessmint.be.global.controllers.validators;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
   String message() default "Invalid phone number";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};
}

class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

   private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

   @Override
   public void initialize(ValidPhoneNumber constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
   }

   @Override
   public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
      if (phoneNumber == null || phoneNumber.trim().isEmpty())
         return true;

      try {
         final var number = phoneNumberUtil.parse(phoneNumber, "ET");
         return phoneNumberUtil.isValidNumber(number);
      } catch (NumberParseException e) {
         return false;
      }
   }
}
