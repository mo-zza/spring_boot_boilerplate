package com.mozza.springboilerplate.service.query.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.service.query.MemberQueryService;
import com.mozza.springboilerplate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberRepository memberRepository;

    @Override
    public MemberResult getByEmail(String email) {
        Member member = memberRepository.findOneByEmail(email);
        if (Objects.isNull(member)) {
            return null;
        }
        return MemberResult.fromEntity(member);
    }

    @Override
    public MemberResult getById(UUID id) {
        Member member =  memberRepository.findOneById(id);
        if (Objects.isNull(member)) {
            return null;
        }
        return MemberResult.fromEntity(member);
    }

    @Override
    public MemberRole getRole(UUID id) {
        Member member = memberRepository.findOneById(id);
        if (Objects.isNull(member)) {
            return null;
        }
        return member.getRole();
    }
}
