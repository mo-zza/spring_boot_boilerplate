package com.mozza.springboilerplate.service.query.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.repository.MemberRepository;
import com.mozza.springboilerplate.repository.PaymentRepository;
import com.mozza.springboilerplate.repository.impl.MemberRepositoryCustomImpl;
import com.mozza.springboilerplate.repository.impl.PaymentRepositoryCustomImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class MemberQueryServiceImplTest {
    @MockBean
    private MemberRepositoryCustomImpl memberRepositoryCustom;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PaymentRepositoryCustomImpl paymentRepositoryCustom;

    @MockBean
    private PaymentRepository paymentRepository;

    MemberQueryServiceImpl memberQueryService;

    @BeforeEach
    public void beforeEach() {
        memberQueryService = new MemberQueryServiceImpl(memberRepository);
    }

    @Nested
    public class GetByEmail {
        @Test
        public void shouldGetMemberByEmail() {
            // given
            String email = "test@dev.com";
            Member member = Member.builder()
                    .name("test")
                    .email(email)
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("test")
                    .build();
            when(memberRepository.findOneByEmail(email)).thenReturn(member);

            // when
            MemberResult result = memberQueryService.getByEmail(email);

            // then
            Assertions.assertNotNull(result);
        }

        @Test
        public void shouldNullByEmail() {
            // given & when
            when(memberRepository.findOneByEmail(any())).thenReturn(null);
            MemberResult result = memberQueryService.getByEmail("");

            // then
            Assertions.assertNull(result);
        }
    }

    @Nested
    public class GetById {
        @Test
        public void shouldGetMemberById() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("test")
                    .build();

            // when
            when(memberRepository.findOneById(member.getId())).thenReturn(member);
            MemberResult result = memberQueryService.getById(member.getId());

            // then
            Assertions.assertNotNull(result);
        }

        @Test
        public void shouldNullMemberGetById() {
            // given & when
            when(memberRepository.findOneById(any())).thenReturn(null);
            MemberResult result = memberQueryService.getById(UUID.randomUUID());

            // then
            Assertions.assertNull(result);
        }
    }

    @Nested
    public class GetRole {
        @Test
        public void shouldGetRole() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("test")
                    .build();

            // when
            when(memberRepository.findOneById(member.getId())).thenReturn(member);
            MemberRole result = memberQueryService.getRole(member.getId());

            // then
            Assertions.assertNotNull(result);
            Assertions.assertEquals(result, MemberRole.USER);
        }

        @Test
        public void shouldNullRoleGetRole() {
            // given & when
            when(memberRepository.findOneById(any())).thenReturn(null);
            MemberRole result = memberQueryService.getRole(UUID.randomUUID());

            // then
            Assertions.assertNull(result);
        }
    }
}
