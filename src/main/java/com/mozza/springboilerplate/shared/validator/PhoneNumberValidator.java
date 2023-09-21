package com.mozza.springboilerplate.shared.validator;

import com.mozza.springboilerplate.shared.annotation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber.matches("^\\d{3}-\\d{3,4}-\\d{4}$");
    }
}
