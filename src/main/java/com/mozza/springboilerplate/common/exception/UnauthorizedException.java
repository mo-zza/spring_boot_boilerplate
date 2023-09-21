package com.mozza.springboilerplate.common.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class UnauthorizedException extends HttpClientErrorException {
    public UnauthorizedException(HttpStatusCode statusCode) {
        super(statusCode);
    }

    public UnauthorizedException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public UnauthorizedException(HttpStatusCode statusCode, String statusText, byte[] body, Charset responseCharset) {
        super(statusCode, statusText, body, responseCharset);
    }
}
