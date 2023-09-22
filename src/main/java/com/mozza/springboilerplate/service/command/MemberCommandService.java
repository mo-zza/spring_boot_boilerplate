package com.mozza.springboilerplate.service.command;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;

import java.util.UUID;

public interface MemberCommandService {
    UUID createWithRole(MemberRequest param, MemberRole role);

    void addPaymentByMemberId(PaymentRequest param, UUID memberId);

    void deletePaymentById(UUID paymentId);

    void validatePasswordByMemberId(String password, UUID memberId);

    void deleteMemberById(UUID memberId);
}
