package com.mozza.springboilerplate.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class BadRequestException extends HttpClientErrorException {
    public BadRequestException(HttpStatusCode statusCode) {
        super(statusCode);
    }

    public BadRequestException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public BadRequestException(HttpStatusCode statusCode, String statusText, byte[] body, Charset responseCharset) {
        super(statusCode, statusText, body, responseCharset);
    }
}
