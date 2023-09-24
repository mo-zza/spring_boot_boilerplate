package com.mozza.springboilerplate.repository;

import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.entity.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentRepositoryCustom {

    Payment findOneById(UUID id);

    Payment findOneByCardNumberAndMemberId(String cardNumber, UUID memberId);

    List<Payment> findAllByMemberId(UUID memberId);

    Long countByMemberId(UUID memberId);
}
