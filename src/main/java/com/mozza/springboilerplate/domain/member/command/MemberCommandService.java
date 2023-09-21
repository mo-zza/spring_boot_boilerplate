package com.mozza.springboilerplate.domain.member.command;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import lombok.NonNull;

public interface MemberCommandService {
    MemberResponse registerWithRole(@NonNull MemberRequest param, @NonNull MemberRole role);
}
