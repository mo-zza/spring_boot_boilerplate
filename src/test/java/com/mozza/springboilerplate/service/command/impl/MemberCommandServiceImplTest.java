package com.mozza.springboilerplate.service.command.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.repository.MemberRepository;
import com.mozza.springboilerplate.repository.PaymentRepository;
import com.mozza.springboilerplate.repository.impl.MemberRepositoryCustomImpl;
import com.mozza.springboilerplate.repository.impl.PaymentRepositoryCustomImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class MemberCommandServiceImplTest {
    @MockBean
    private MemberRepositoryCustomImpl memberRepositoryCustom;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PaymentRepositoryCustomImpl paymentRepositoryCustom;

    @MockBean
    private PaymentRepository paymentRepository;

    private MemberCommandServiceImpl memberCommandService;

    @BeforeEach
    public void beforeEach() {
        memberCommandService = new MemberCommandServiceImpl(memberRepository, paymentRepository);
    }

    @Nested
    class CreateWithRoleTest {
        @Test
        public void shouldCreateMemberWithRole () {
            // given
            MemberRequest member = new MemberRequest()
                    .setName("member command test 2")
                    .setEmail("command@test2.com")
                    .setPhoneNumber("000-1111-1112")
                    .setPassword("member_command_test");
            Member entity = member.toEntityWithRole(MemberRole.USER);
            Mockito.when(memberRepository.save(any(Member.class))).thenReturn(entity);

            // when
            UUID saveMemberId = memberCommandService.createWithRole(member, MemberRole.USER);

            // then
            Assertions.assertEquals(saveMemberId, entity.getId());
        }
    }
}