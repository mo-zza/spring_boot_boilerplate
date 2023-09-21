package com.mozza.springboilerplate.domain.member.query;

import com.mozza.springboilerplate.domain.member.dto.MemberCond;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MemberQueryService {
    MemberResponse getByEmail(String email);

    MemberResponse getById(UUID id);

    MemberResponse getByPhoneNumber(String phoneNumber);

    Page<MemberResponse> getAllByCond(MemberCond cond);
}
