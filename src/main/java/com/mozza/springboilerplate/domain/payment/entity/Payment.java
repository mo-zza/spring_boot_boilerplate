package com.mozza.springboilerplate.domain.payment.entity;

import com.mozza.springboilerplate.common.entity.BaseEntity;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_payment_id", columnList = "id", unique = true),
        @Index(name = "idx_payment_method", columnList = "method"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(unique = true)
    private String cardNumber;

    @Column
    private String cardExpirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Payment(@NonNull PaymentMethod method, String cardNumber, String cardExpirationDate) {
        this.method = method;
        this.cardNumber = cardNumber;
        this.cardExpirationDate = cardExpirationDate;
    }
}
