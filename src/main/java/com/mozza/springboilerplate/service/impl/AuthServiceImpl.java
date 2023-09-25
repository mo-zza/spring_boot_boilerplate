package com.mozza.springboilerplate.service.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.auth.dto.TokenResponse;
import com.mozza.springboilerplate.auth.jwt.JwtTokenProvider;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.dto.SignInRequest;
import com.mozza.springboilerplate.domain.member.exception.AlreadyExistException;
import com.mozza.springboilerplate.domain.member.exception.NotFoundMemberException;
import com.mozza.springboilerplate.domain.member.exception.UnauthorizedRoleException;
import com.mozza.springboilerplate.service.AuthService;
import com.mozza.springboilerplate.service.command.MemberCommandService;
import com.mozza.springboilerplate.service.query.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse registerUser(MemberRequest param) {
        validateMemberNotExist(param.getEmail());

        UUID id = memberCommandService.createWithRole(param, MemberRole.USER);
        return getResponseByMemberId(id, MemberRole.USER);
    }

    @Override
    public TokenResponse registerAdminByMemberId(MemberRequest param, UUID requestedMemberId) {
        validateMemberNotExist(param.getEmail());

        assertCreateAdminAuthorityByMemberId(requestedMemberId);

        UUID id = memberCommandService.createWithRole(param, MemberRole.ADMIN);
        return getResponseByMemberId(id, MemberRole.ADMIN);
    }

    @Override
    public TokenResponse registerDevByMemberId(MemberRequest param, UUID requestedMember) {
        validateMemberNotExist(param.getEmail());

        assertCreateDevAuthorityByMemberId(requestedMember);

        UUID id = memberCommandService.createWithRole(param, MemberRole.DEV);
        return getResponseByMemberId(id, MemberRole.DEV);
    }

    @Override
    public TokenResponse validateMember(SignInRequest param) {
        MemberResult member = memberQueryService.getByEmail(param.getEmail());
        if (Objects.isNull(member)) {
            throw new NotFoundMemberException("존재하지 않는 회원입니다.");
        }

        memberCommandService.validatePasswordByMemberId(param.getPassword(), member.getId());

        return getResponseByMemberId(member.getId(), member.getRole());
    }

    private void validateMemberNotExist(String email) {
        MemberResult isExists = memberQueryService.getByEmail(email);
        if (Objects.nonNull(isExists)) {
            throw new AlreadyExistException("이미 존재하는 이메일입니다.");
        }
    }

    private TokenResponse getResponseByMemberId(UUID memberId, MemberRole role) {
        return jwtTokenProvider.createToken(memberId, role);
    }

    private void assertCreateDevAuthorityByMemberId(UUID memberId) {
        MemberRole role = memberQueryService.getRole(memberId);
        if (!role.equals(MemberRole.DEV)) {
            throw new UnauthorizedRoleException("개발자 권한을 생성할 수 있는 권한이 없습니다.");
        }
    }

    private void assertCreateAdminAuthorityByMemberId(UUID memberId) {
        MemberRole role = memberQueryService.getRole(memberId);
        if (role.equals(MemberRole.USER)) {
            throw new UnauthorizedRoleException("어드민 계정을 생성할 수 있는 권한이 없습니다.");
        }
    }
}
