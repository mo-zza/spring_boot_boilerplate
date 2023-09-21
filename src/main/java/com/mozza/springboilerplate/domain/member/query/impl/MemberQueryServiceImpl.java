package com.mozza.springboilerplate.domain.member.query.impl;

import com.mozza.springboilerplate.domain.member.dto.MemberCond;
import com.mozza.springboilerplate.domain.member.query.MemberQueryService;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;

    @Override
    public MemberResponse getByEmail(String email) {
        return memberRepository.findOneByEmail(email);
    }

    @Override
    public MemberResponse getById(UUID id) {
        return memberRepository.findOneById(id);
    }

    @Override
    public MemberResponse getByPhoneNumber(String phoneNumber) {
        return memberRepository.findOneByPhoneNumber(phoneNumber);
    }

    @Override
    public Page<MemberResponse> getAllByCond(MemberCond cond) {
        return memberRepository.findAll(cond);
    }
}
