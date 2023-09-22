package com.mozza.springboilerplate.domain.member.exception;

import com.mozza.springboilerplate.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends NotFoundException {
    public NotFoundMemberException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
