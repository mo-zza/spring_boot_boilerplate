package com.mozza.springboilerplate.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentResponse {
    private UUID id;

    private String method;

    private String cardNumber;

    private String cardExpirationDate;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @QueryProjection
    public PaymentResponse(UUID id, String method, String cardNumber,
                           String cardExpirationDate, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.method = method;
        this.cardNumber = cardNumber;
        this.cardExpirationDate = cardExpirationDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
