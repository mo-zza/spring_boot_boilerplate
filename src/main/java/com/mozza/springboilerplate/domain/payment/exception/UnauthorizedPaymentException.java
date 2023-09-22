package com.mozza.springboilerplate.domain.payment.exception;

import com.mozza.springboilerplate.common.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;

public class UnauthorizedPaymentException extends UnauthorizedException {
    public UnauthorizedPaymentException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
