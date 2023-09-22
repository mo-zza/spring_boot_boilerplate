package com.mozza.springboilerplate.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentResponse {
    private UUID id;

    private com.mozza.springboilerplate.domain.payment.constant.PaymentMethod method;

    private String cardNumber;

    private String cardExpirationDate;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public static PaymentResponse fromEntity(Payment entity) {
        return new PaymentResponse()
                .setId(entity.getId())
                .setMethod(entity.getMethod())
                .setCardNumber(entity.getCardNumber())
                .setCardExpirationDate(entity.getCardExpirationDate())
                .setCreatedDate(entity.getCreatedDate())
                .setModifiedDate(entity.getModifiedDate());
    }

    public static PaymentResponse fromResult(PaymentResult result) {
        return new PaymentResponse()
                .setId(result.getId())
                .setMethod(result.getMethod())
                .setCardNumber(result.getCardNumber())
                .setCardExpirationDate(result.getCardExpirationDate())
                .setCreatedDate(result.getCreatedDate())
                .setModifiedDate(result.getModifiedDate());
    }
}
