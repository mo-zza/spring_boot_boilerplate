package com.mozza.springboilerplate.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentsWithCountResponse {
    private List<PaymentResponse> payments;

    private int count;

    public static PaymentsWithCountResponse fromResultAndCount(List<PaymentResult> payments, Long count) {
        return new PaymentsWithCountResponse()
                .setPayments(payments.stream()
                        .map(PaymentResponse::fromResult)
                        .collect(Collectors.toList()))
                .setCount(count.intValue());
    }
}
