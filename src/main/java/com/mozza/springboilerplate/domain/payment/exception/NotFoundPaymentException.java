package com.mozza.springboilerplate.domain.payment.exception;

import com.mozza.springboilerplate.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class NotFoundPaymentException extends NotFoundException {
    public NotFoundPaymentException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
