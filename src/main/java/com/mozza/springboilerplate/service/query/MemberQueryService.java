package com.mozza.springboilerplate.service.query;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;

import java.util.UUID;

public interface MemberQueryService {
    MemberResult getByEmail(String email);

    MemberResult getById(UUID id);

    MemberRole getRole(UUID id);
}
