package com.mozza.springboilerplate.domain.member.exception;

import com.mozza.springboilerplate.common.exception.UnauthorizedException;
import org.springframework.http.HttpStatusCode;

public class UnauthorizedPasswordException extends UnauthorizedException {
    public UnauthorizedPasswordException() {
        super(HttpStatusCode.valueOf(401), "비밀번호가 일치하지 않습니다.");
    }
}
