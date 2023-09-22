package com.mozza.springboilerplate.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class NotFoundException extends HttpClientErrorException {
    public NotFoundException(HttpStatusCode statusCode) {
        super(statusCode);
    }

    public NotFoundException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public NotFoundException(HttpStatusCode statusCode, String statusText, byte[] body, Charset responseCharset) {
        super(statusCode, statusText, body, responseCharset);
    }
}
