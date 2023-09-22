package com.mozza.springboilerplate.service.query.impl;

import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.repository.PaymentRepository;
import com.mozza.springboilerplate.service.query.PaymentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResult getById(UUID id) {
        return paymentRepository.findOneById(id);
    }

    @Override
    public List<PaymentResult> getAllByMemberId(UUID memberId) {
        return paymentRepository.findAllByMemberId(memberId);
    }

    @Override
    public Long getCountByMemberId(UUID memberId) {
        return paymentRepository.countByMemberId(memberId);
    }
}
