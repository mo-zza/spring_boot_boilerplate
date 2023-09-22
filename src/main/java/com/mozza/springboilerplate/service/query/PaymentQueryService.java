package com.mozza.springboilerplate.service.query;

import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;

import java.util.List;
import java.util.UUID;

public interface PaymentQueryService {

    PaymentResult getById(UUID id);
    List<PaymentResult> getAllByMemberId(UUID memberId);

    Long getCountByMemberId(UUID memberId);
}
