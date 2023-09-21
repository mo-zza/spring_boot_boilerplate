package com.mozza.springboilerplate.domain.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResponse {
    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;

    private Set<Payment> payments;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @QueryProjection
    public MemberResponse(UUID id, String name, String email,
                          String phoneNumber, Set<Payment> payments, LocalDateTime createdDate,
                          LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.payments = payments;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static MemberResponse fromEntity(Member entity) {
        return new MemberResponse()
                .setId(entity.getId())
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setPhoneNumber(entity.getPhoneNumber())
                .setPayments(entity.getPayments())
                .setCreatedDate(entity.getCreatedDate())
                .setModifiedDate(entity.getModifiedDate());
    }
}
