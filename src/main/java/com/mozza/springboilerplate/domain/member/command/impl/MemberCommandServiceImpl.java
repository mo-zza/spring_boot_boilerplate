package com.mozza.springboilerplate.domain.member.command.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.command.MemberCommandService;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {
    private final MemberRepository memberRepository;

    @Override
    public MemberResponse registerWithRole(MemberRequest param, MemberRole role) {
        Member member = memberRepository.save(param.toEntityWithRole(MemberRole.USER));
        return MemberResponse.fromEntity(member);
    }
}
