package com.mozza.springboilerplate.domain.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResult {
    private UUID id;

    private MemberRole role;

    private String name;

    private String email;

    private String phoneNumber;

    private String encryptedPassword;

    private String salt;

    private Set<Payment> payments;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @QueryProjection
    public MemberResult(UUID id, String name, MemberRole role,
                        String email, String phoneNumber, Set<Payment> payments,
                        String encryptedPassword, String salt, LocalDateTime createdDate,
                        LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.payments = payments;
        this.encryptedPassword = encryptedPassword;
        this.salt = salt;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static MemberResult fromEntity(Member entity) {
        return new MemberResult()
                .setId(entity.getId())
                .setName(entity.getName())
                .setRole(entity.getRole())
                .setEmail(entity.getEmail())
                .setPhoneNumber(entity.getPhoneNumber())
                .setPayments(entity.getPayments())
                .setEncryptedPassword(entity.getEncryptedPassword())
                .setSalt(entity.getSalt())
                .setCreatedDate(entity.getCreatedDate())
                .setModifiedDate(entity.getModifiedDate());
    }
}
