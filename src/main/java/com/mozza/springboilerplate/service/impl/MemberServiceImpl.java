package com.mozza.springboilerplate.service.impl;

import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;
import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.dto.PaymentsWithCountResponse;
import com.mozza.springboilerplate.domain.payment.exception.NotFoundPaymentException;
import com.mozza.springboilerplate.domain.payment.exception.UnauthorizedPaymentException;
import com.mozza.springboilerplate.service.command.MemberCommandService;
import com.mozza.springboilerplate.service.MemberService;
import com.mozza.springboilerplate.service.query.MemberQueryService;
import com.mozza.springboilerplate.service.query.PaymentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final PaymentQueryService paymentQueryService;

    @Override
    public MemberResponse getMemberById(UUID memberId) {
        MemberResult member = memberQueryService.getById(memberId);
        if (Objects.isNull(member)) {
            return null;
        }
        return MemberResponse.fromResult(member);
    }

    @Override
    public PaymentsWithCountResponse getPaymentWithCountByMemberId(UUID memberId) {
        List<PaymentResult> payments = paymentQueryService.getAllByMemberId(memberId);
        if (payments.isEmpty()) {
            return PaymentsWithCountResponse.fromResultAndCount(payments, 0L);
        }
        Long count = paymentQueryService.getCountByMemberId(memberId);
        return PaymentsWithCountResponse.fromResultAndCount(payments, count);
    }

    @Override
    public PaymentsWithCountResponse addPaymentByMemberId(PaymentRequest param, UUID memberId) {
        memberCommandService.addPaymentByMemberId(param, memberId);
        return getPaymentWithCountByMemberId(memberId);
    }

    @Override
    public PaymentsWithCountResponse deletePaymentByMemberId(UUID paymentId, UUID memberId) {
        PaymentResult payment = paymentQueryService.getById(paymentId);
        if (Objects.isNull(payment)) {
            throw new NotFoundPaymentException("존재하지 않는 결제 수단입니다.");
        }
        assertPaymentAuthorityByMemberId(payment, memberId);
        memberCommandService.deletePaymentById(paymentId);
        return getPaymentWithCountByMemberId(paymentId);
    }

    @Override
    public void deleteMemberById(UUID memberId) {
        memberCommandService.deleteMemberById(memberId);
    }

    private void assertPaymentAuthorityByMemberId(PaymentResult payment, UUID memberId) {
        if (!payment.getMember().getId().equals(memberId)) {
            throw new UnauthorizedPaymentException("결제 수단에 대한 권한이 없습니다.");
        }
    }
}
