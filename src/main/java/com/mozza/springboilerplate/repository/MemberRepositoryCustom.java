package com.mozza.springboilerplate.repository;

import com.mozza.springboilerplate.domain.member.dto.MemberCond;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MemberRepositoryCustom {
    MemberResponse findOneByEmail(String email);

    MemberResponse findOneByPhoneNumber(String phoneNumber);

    MemberResponse findOneById(UUID id);

    Page<MemberResponse> findAll(MemberCond cond);
}
