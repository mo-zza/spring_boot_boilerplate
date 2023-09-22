package com.mozza.springboilerplate.domain.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.domain.payment.dto.PaymentResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Accessors(chain = true)
public class MemberResponse {
    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;

    private List<PaymentResponse> payments;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public static MemberResponse fromResult(MemberResult output) {
        return new MemberResponse()
                .setId(output.getId())
                .setName(output.getName())
                .setEmail(output.getEmail())
                .setPhoneNumber(output.getPhoneNumber())
                .setPayments(output.getPayments().stream()
                        .map(PaymentResponse::fromEntity)
                        .collect(Collectors.toList()))
                .setCreatedDate(output.getCreatedDate())
                .setModifiedDate(output.getModifiedDate());
    }
}
