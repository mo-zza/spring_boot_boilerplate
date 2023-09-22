package com.mozza.springboilerplate.service.command.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.exception.AlreadyExistException;
import com.mozza.springboilerplate.domain.member.exception.NotFoundMemberException;
import com.mozza.springboilerplate.domain.member.exception.UnauthorizedPasswordException;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.repository.PaymentRepository;
import com.mozza.springboilerplate.service.command.MemberCommandService;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public UUID createWithRole(MemberRequest param, MemberRole role) {
        Member member = memberRepository.save(param.toEntityWithRole(MemberRole.USER));
        return member.getId();
    }

    @Override
    public void addPaymentByMemberId(PaymentRequest param, UUID memberId) {
        Member member = memberRepository.findOneById(memberId);
        if (Objects.isNull(member)) {
            throw new NotFoundMemberException("존재하지 않는 회원입니다.");
        }

        Payment exists = paymentRepository.findOneByCardNumberAndMemberId(param.getCardNumber(), memberId);
        if (Objects.nonNull(exists)) {
            throw new AlreadyExistException("이미 존재하는 카드번호입니다.");
        }

        memberRepository.save(member.addPayment(param.toEntity()));
    }

    @Override
    public void deletePaymentById(UUID paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    @Override
    public void validatePasswordByMemberId(String password, UUID memberId) {
        Member member = memberRepository.findOneById(memberId);

        try {
            boolean validation = member.validatePassword(password);
            if (!validation) {
                throw new UnauthorizedPasswordException();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("비밀번호 암호화에 실패했습니다.");
        }
    }

    @Override
    public void deleteMemberById(UUID memberId) {
        memberRepository.deleteById(memberId);
    }
}
