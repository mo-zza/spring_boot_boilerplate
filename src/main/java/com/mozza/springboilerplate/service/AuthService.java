package com.mozza.springboilerplate.service;

import com.mozza.springboilerplate.auth.dto.TokenResponse;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.dto.SignInRequest;

import java.util.UUID;

public interface AuthService {
    TokenResponse registerUser(MemberRequest param);

    TokenResponse registerAdminByMemberId(MemberRequest param, UUID requestedMemberId);

    TokenResponse registerDevByMemberId(MemberRequest param, UUID requestedMemberId);

    TokenResponse validateMember(SignInRequest param);
}
