package com.mozza.springboilerplate.domain.member.dto;

import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.shared.request.BaseQueryParam;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MemberCond extends BaseQueryParam {
    private String name;

    private String email;

    private String phoneNumber;

    private PaymentMethod paymentMethod;
}
