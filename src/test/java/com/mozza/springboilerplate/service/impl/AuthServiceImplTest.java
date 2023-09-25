package com.mozza.springboilerplate.service.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.auth.dto.TokenResponse;
import com.mozza.springboilerplate.auth.jwt.JwtTokenProvider;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.dto.SignInRequest;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.member.exception.AlreadyExistException;
import com.mozza.springboilerplate.domain.member.exception.NotFoundMemberException;
import com.mozza.springboilerplate.domain.member.exception.UnauthorizedPasswordException;
import com.mozza.springboilerplate.domain.member.exception.UnauthorizedRoleException;
import com.mozza.springboilerplate.repository.impl.MemberRepositoryCustomImpl;
import com.mozza.springboilerplate.repository.impl.PaymentRepositoryCustomImpl;
import com.mozza.springboilerplate.service.AuthService;
import com.mozza.springboilerplate.service.command.MemberCommandService;
import com.mozza.springboilerplate.service.query.MemberQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class AuthServiceImplTest {
    @MockBean
    private MemberRepositoryCustomImpl memberRepositoryCustom;

    @MockBean
    private PaymentRepositoryCustomImpl paymentRepositoryCustom;

    @MockBean
    private MemberCommandService memberCommandService;

    @MockBean
    private MemberQueryService memberQueryService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;

    @BeforeEach
    public void beforeEach() {
        authService = new AuthServiceImpl(memberCommandService, memberQueryService, jwtTokenProvider);
    }

    @Nested
    public class RegisterUser {
        @Test
        public void shouldRegisterUser() {
            // given
            MemberRequest param = getMemberRequest();
            UUID memberId = UUID.randomUUID();
            MemberRole userRole = MemberRole.USER;
            TokenResponse authTokens = getTokenResponse();

            // when
            when(memberQueryService.getByEmail(param.getEmail())).thenReturn(null);
            when(memberCommandService.createWithRole(param, userRole)).thenReturn(memberId);
            when(jwtTokenProvider.createToken(memberId, userRole)).thenReturn(authTokens);
            TokenResponse response = authService.registerUser(param);

            // then
            Assertions.assertTrue(response.getAccessToken().length() > 0);
        }

        @Test
        public void shouldAlreadyExistException() {
            // given
            MemberRequest param = getMemberRequest();
            Member member = getMemberEntityWithRole(MemberRole.USER);
            MemberResult existMember = getMemberResult(member);

            // when
            when(memberQueryService.getByEmail(param.getEmail())).thenReturn(existMember);
            Assertions.assertThrows(AlreadyExistException.class, () -> authService.registerUser(param));
        }
    }

    @Nested
    public class RegisterAdminByMemberId {
        @Test
        public void shouldRegisterAdmin() {
            // given
            MemberRequest param = getMemberRequest();
            MemberRole userRole = MemberRole.ADMIN;
            Member member = getMemberEntityWithRole(userRole);
            TokenResponse authTokens = getTokenResponse();

            // when
            when(memberQueryService.getByEmail(param.getEmail())).thenReturn(null);
            when(memberCommandService.createWithRole(param, userRole)).thenReturn(member.getId());
            when(jwtTokenProvider.createToken(member.getId(), userRole)).thenReturn(authTokens);
            when(memberQueryService.getRole(member.getId())).thenReturn(member.getRole());
            TokenResponse response = authService.registerAdminByMemberId(param, member.getId());

            // then
            Assertions.assertTrue(response.getAccessToken().length() > 0);
        }

        @Test
        public void shouldUnauthorizedRoleException() {
            // given
            MemberRequest param = getMemberRequest();
            MemberRole userRole = MemberRole.USER;
            Member member = getMemberEntityWithRole(userRole);
            TokenResponse authTokens = getTokenResponse();

            // when
            when(memberQueryService.getByEmail(param.getEmail())).thenReturn(null);
            when(memberCommandService.createWithRole(param, userRole)).thenReturn(member.getId());
            when(jwtTokenProvider.createToken(member.getId(), userRole)).thenReturn(authTokens);
            when(memberQueryService.getRole(member.getId())).thenReturn(member.getRole());
            Assertions.assertThrows(UnauthorizedRoleException.class, () -> {
                authService.registerAdminByMemberId(param, member.getId());
            });
        }

        @Test
        public void shouldDevRoleCanRegisterAdmin() {
            // given
            MemberRequest param = getMemberRequest();
            MemberRole userRole = MemberRole.ADMIN;
            Member member = getMemberEntityWithRole(MemberRole.DEV);
            TokenResponse authTokens = getTokenResponse();

            // when
            when(memberQueryService.getByEmail(param.getEmail())).thenReturn(null);
            when(memberCommandService.createWithRole(param, userRole)).thenReturn(member.getId());
            when(jwtTokenProvider.createToken(member.getId(), userRole)).thenReturn(authTokens);
            when(memberQueryService.getRole(member.getId())).thenReturn(member.getRole());
            TokenResponse response = authService.registerAdminByMemberId(param, member.getId());

            // then
            Assertions.assertTrue(response.getAccessToken().length() > 0);
        }
    }

    @Nested
    public class RegisterDevByMemberId {
        @Test
        public void shouldRegisterDevMember() {
            // given
            MemberRequest param = getMemberRequest();
            MemberRole userRole = MemberRole.DEV;
            Member member = getMemberEntityWithRole(userRole);
            TokenResponse authTokens = getTokenResponse();

            // when
            when(memberQueryService.getByEmail(param.getEmail())).thenReturn(null);
            when(memberCommandService.createWithRole(param, userRole)).thenReturn(member.getId());
            when(jwtTokenProvider.createToken(member.getId(), userRole)).thenReturn(authTokens);
            when(memberQueryService.getRole(member.getId())).thenReturn(member.getRole());
            TokenResponse response = authService.registerDevByMemberId(param, member.getId());

            // then
            Assertions.assertTrue(response.getAccessToken().length() > 0);
        }

        @Test
        public void shouldAdminNotRegisterDev() {
            // given
            MemberRequest param = getMemberRequest();
            MemberRole userRole = MemberRole.DEV;
            Member member = getMemberEntityWithRole(MemberRole.ADMIN);
            TokenResponse authTokens = getTokenResponse();

            // when
            when(memberQueryService.getByEmail(param.getEmail())).thenReturn(null);
            when(memberCommandService.createWithRole(param, userRole)).thenReturn(member.getId());
            when(jwtTokenProvider.createToken(member.getId(), userRole)).thenReturn(authTokens);
            when(memberQueryService.getRole(member.getId())).thenReturn(member.getRole());

            // when & then
            Assertions.assertThrows(UnauthorizedRoleException.class, () -> {
                authService.registerDevByMemberId(param, member.getId());
            });
        }
    }

    @Nested
    public class ValidateMember {
        @Test
        public void shouldValidateMember() {
            // given
            Member member = getMemberEntityWithRole(MemberRole.USER);
            MemberResult memberResult = getMemberResult(member);
            SignInRequest signInRequest =  new SignInRequest()
                    .setEmail(member.getEmail())
                    .setPassword("password");
            TokenResponse authTokens = getTokenResponse();

            // when
            when(memberQueryService.getByEmail(signInRequest.getEmail())).thenReturn(memberResult);
            doNothing().when(memberCommandService).validatePasswordByMemberId(signInRequest.getPassword(), member.getId());
            when(jwtTokenProvider.createToken(member.getId(), member.getRole())).thenReturn(authTokens);
            TokenResponse response = authService.validateMember(signInRequest);

            // then
            Assertions.assertTrue(response.getAccessToken().length() > 0);
        }

        @Test
        public void shouldNotFoundMember() {
            // given
            Member member = getMemberEntityWithRole(MemberRole.USER);
            SignInRequest signInRequest =  new SignInRequest()
                    .setEmail(member.getEmail())
                    .setPassword("password");

            // when
            when(memberQueryService.getByEmail(signInRequest.getEmail())).thenReturn(null);
            Assertions.assertThrows(NotFoundMemberException.class, () -> {
                authService.validateMember(signInRequest);
            });
        }

        @Test
        public void shouldUnauthorizedPassword() {
            // given
            Member member = getMemberEntityWithRole(MemberRole.USER);
            MemberResult memberResult = getMemberResult(member);
            SignInRequest signInRequest =  new SignInRequest()
                    .setEmail(member.getEmail())
                    .setPassword("password");

            // when
            when(memberQueryService.getByEmail(signInRequest.getEmail())).thenReturn(memberResult);
            doThrow(UnauthorizedPasswordException.class).when(memberCommandService).validatePasswordByMemberId(signInRequest.getPassword(), member.getId());
            Assertions.assertThrows(UnauthorizedPasswordException.class, () -> {
                authService.validateMember(signInRequest);
            });
        }
    }

    private MemberRequest getMemberRequest() {
        return new MemberRequest()
                .setName("test")
                .setEmail("dev@email.com")
                .setPassword("test")
                .setPhoneNumber("000-0000-0000");
    }

    private Member getMemberEntityWithRole(MemberRole role) {
        return Member.builder()
                .name("test")
                .email("test@dev.com")
                .role(role)
                .phoneNumber("000-0000-0000")
                .password("password")
                .build();
    }

    private MemberResult getMemberResult(Member member) {
        return MemberResult.fromEntity(member);
    }

    private TokenResponse getTokenResponse() {
        return TokenResponse.from("access_token", "refresh_token");
    }
}
