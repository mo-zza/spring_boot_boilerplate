package com.mozza.springboilerplate.service;

import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;
import com.mozza.springboilerplate.domain.payment.dto.PaymentsWithCountResponse;

import java.util.UUID;

public interface MemberService {
    MemberResponse getMemberById(UUID memberId);

    PaymentsWithCountResponse getPaymentWithCountByMemberId(UUID memberId);

    PaymentsWithCountResponse addPaymentByMemberId(PaymentRequest param, UUID memberId);

    PaymentsWithCountResponse deletePaymentByMemberId(UUID paymentId, UUID memberId);

    void deleteMemberById(UUID memberId);
}
