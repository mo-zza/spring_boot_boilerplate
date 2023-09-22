package com.mozza.springboilerplate.domain.member.exception;

import com.mozza.springboilerplate.common.exception.BadRequestException;
import org.springframework.http.HttpStatus;

public class AlreadyExistException extends BadRequestException {
    public AlreadyExistException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
