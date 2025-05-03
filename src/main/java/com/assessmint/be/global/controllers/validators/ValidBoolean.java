package com.assessmint.be.global.controllers.validators;

import com.assessmint.be.global.Utils;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BooleanValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBoolean {
    String message() default "Invalid Boolean Value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class BooleanValidator implements ConstraintValidator<ValidBoolean, String> {

    @Override
    public void initialize(ValidBoolean constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String bul, ConstraintValidatorContext context) {
        if (bul == null || bul.trim().isEmpty()) {
            return true;
        }

        final var bulVal = bul.trim();

        return Utils.parseBoolean(bulVal) != null;
    }
}
