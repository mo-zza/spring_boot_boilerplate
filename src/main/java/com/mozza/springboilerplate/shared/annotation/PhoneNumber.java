package com.mozza.springboilerplate.shared.annotation;

import com.mozza.springboilerplate.shared.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "phone number is invalid";

    Class[] groups() default {};

    Class[] payload() default {};
}
