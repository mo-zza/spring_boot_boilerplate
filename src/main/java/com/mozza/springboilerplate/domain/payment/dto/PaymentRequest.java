package com.mozza.springboilerplate.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentRequest {
    @NotNull
    private PaymentMethod method;

    private String cardNumber;

    @DateTimeFormat(pattern = "MM/yy")
    private String cardExpirationDate;

    public Payment toEntity(Member member) {
        return Payment.builder()
                .method(method)
                .cardNumber(cardNumber)
                .cardExpirationDate(cardExpirationDate)
                .member(member)
                .build();
    }
}
