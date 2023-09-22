package com.mozza.springboilerplate.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {

    public static <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> created(T body) {
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<T> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
