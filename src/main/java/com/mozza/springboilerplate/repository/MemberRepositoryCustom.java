package com.mozza.springboilerplate.repository;

import com.mozza.springboilerplate.domain.member.dto.MemberCond;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.entity.Member;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MemberRepositoryCustom {
    Member findOneByEmail(String email);

    Member findOneById(UUID id);

    Page<MemberResult> findAll(MemberCond cond);
}
