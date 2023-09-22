package com.mozza.springboilerplate.domain.member.exception;

import com.mozza.springboilerplate.common.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;

public class UnauthorizedRoleException extends UnauthorizedException {
    public UnauthorizedRoleException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
