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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Constraint(validatedBy = DateTimeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateTime {
    String message() default "Invalid date time format (yyyy-MM-dd HH:mm)";

    String pattern() default "yyyy-MM-dd HH:mm";  // Default pattern

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class DateTimeValidator implements ConstraintValidator<ValidDateTime, String> {

    private String pattern;

    @Override
    public void initialize(ValidDateTime constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String dateTime, ConstraintValidatorContext context) {
        if (dateTime == null || dateTime.trim().isEmpty()) {
            return true;
        }

        try {
            LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
