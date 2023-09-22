package com.mozza.springboilerplate.domain.member.exception;

import com.mozza.springboilerplate.common.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;

public class UnauthorizedPasswordException extends UnauthorizedException {
    public UnauthorizedPasswordException() {
        super(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
    }
}
