package com.mozza.springboilerplate.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentResult {
    private UUID id;

    private UUID memberId;

    private PaymentMethod method;

    private String cardNumber;

    private String cardExpirationDate;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @QueryProjection
    public PaymentResult(UUID id, UUID memberId,PaymentMethod method,
                         String cardNumber, String cardExpirationDate, LocalDateTime createdDate,
                         LocalDateTime modifiedDate) {
        this.id = id;
        this.memberId = memberId;
        this.method = method;
        this.cardNumber = cardNumber;
        this.cardExpirationDate = cardExpirationDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
