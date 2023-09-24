package com.mozza.springboilerplate.service.query.impl;

import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.repository.PaymentRepository;
import com.mozza.springboilerplate.service.query.PaymentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResult getById(UUID id) {
        Payment payment = paymentRepository.findOneById(id);
        if (Objects.isNull(payment)) {
            return null;
        }

        return PaymentResult.fromEntity(payment);
    }

    @Override
    public List<PaymentResult> getAllByMemberId(UUID memberId) {
        List<Payment> payments = paymentRepository.findAllByMemberId(memberId);
        if (payments.isEmpty()) {
            return null;
        }

        return payments.stream()
                .map(PaymentResult::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Long getCountByMemberId(UUID memberId) {
        return paymentRepository.countByMemberId(memberId);
    }
}
