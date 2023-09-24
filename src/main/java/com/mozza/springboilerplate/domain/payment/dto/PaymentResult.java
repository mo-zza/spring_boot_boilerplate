package com.mozza.springboilerplate.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentResult {
    private UUID id;

    private Member member;

    private PaymentMethod method;

    private String cardNumber;

    private String cardExpirationDate;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @QueryProjection
    public PaymentResult(UUID id, Member member, PaymentMethod method,
                         String cardNumber, String cardExpirationDate, LocalDateTime createdDate,
                         LocalDateTime modifiedDate) {
        this.id = id;
        this.member = member;
        this.method = method;
        this.cardNumber = cardNumber;
        this.cardExpirationDate = cardExpirationDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static PaymentResult fromEntity(Payment payment) {
        return new PaymentResult()
                .setId(payment.getId())
                .setMember(payment.getMember())
                .setMethod(payment.getMethod())
                .setCardNumber(payment.getCardNumber())
                .setCardExpirationDate(payment.getCardExpirationDate())
                .setCreatedDate(payment.getCreatedDate())
                .setModifiedDate(payment.getModifiedDate());
    }
}
