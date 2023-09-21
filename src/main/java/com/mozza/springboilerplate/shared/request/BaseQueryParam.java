package com.mozza.springboilerplate.shared.request;

import lombok.Getter;

@Getter
public class BaseQueryParam {
    private int page = 0;

    private int size = 1000;
}
