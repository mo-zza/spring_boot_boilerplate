package com.mozza.springboilerplate.domain.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.shared.annotation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberRequest {
    @NotEmpty(message = "name is required")
    private String name;

    @Email(message = "email is invalid")
    @NotEmpty(message = "email is required")
    private String email;

    @PhoneNumber
    @NotEmpty(message = "phone_number is required")
    private String phoneNumber;

    @NotEmpty(message = "password is required")
    private String password;

    public Member toEntityWithRole(MemberRole role) {
        return Member.builder()
                .name(name)
                .role(role)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }
}
