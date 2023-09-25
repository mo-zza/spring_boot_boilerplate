package com.mozza.springboilerplate.service.command.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.member.exception.AlreadyExistException;
import com.mozza.springboilerplate.domain.member.exception.NotFoundMemberException;
import com.mozza.springboilerplate.domain.member.exception.UnauthorizedPasswordException;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.domain.payment.exception.NotFoundPaymentException;
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

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public class CreateWithRoleTest {
        @Test
        public void shouldCreateMemberWithRole() {
            // given
            MemberRequest member = new MemberRequest()
                    .setName("member command test 2")
                    .setEmail("command@test2.com")
                    .setPhoneNumber("000-1111-1112")
                    .setPassword("member_command_test");
            Member entity = member.toEntityWithRole(MemberRole.USER);
            when(memberRepository.save(any(Member.class))).thenReturn(entity);

            // when
            UUID saveMemberId = memberCommandService.createWithRole(member, MemberRole.USER);

            // then
            Assertions.assertEquals(saveMemberId, entity.getId());
        }
    }

    @Nested
    public class AddPaymentByMemberId {
        @Test
        public void shouldAddPaymentByMemberId() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("test")
                    .build();
            PaymentRequest paymentRequest = new PaymentRequest()
                    .setMethod(PaymentMethod.CARD)
                    .setCardNumber("1234-1234-1234-1234")
                    .setCardExpirationDate("12/34");

            // when
            when(memberRepository.findOneById(member.getId())).thenReturn(member);
            when(paymentRepository.findOneByCardNumberAndMemberId(paymentRequest.getCardNumber(), member.getId())).thenReturn(null);
            memberCommandService.addPaymentByMemberId(paymentRequest, member.getId());

            // then
            Mockito.verify(memberRepository, Mockito.times(1)).save(any(Member.class));
        }

        @Test
        public void shouldThrowNotFoundException() {
            // given & when
            when(memberRepository.findOneById(any())).thenReturn(null);
            Assertions.assertThrows(NotFoundMemberException.class, () -> {
                memberCommandService.addPaymentByMemberId(new PaymentRequest(), UUID.randomUUID());
            });
        }

        @Test
        public void shouldThrowAlreadyExistException() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("test")
                    .build();
            PaymentRequest paymentRequest = new PaymentRequest()
                    .setMethod(PaymentMethod.CARD)
                    .setCardNumber("1234-1234-1234-1234")
                    .setCardExpirationDate("12/34");
            Payment payment = Payment.builder()
                    .method(PaymentMethod.CARD)
                    .member(member)
                    .cardNumber("1234-1234-1234-1234")
                    .cardExpirationDate("12/34")
                    .build();

            // when
            when(memberRepository.findOneById(member.getId())).thenReturn(member);
            when(paymentRepository.findOneByCardNumberAndMemberId(paymentRequest.getCardNumber(), member.getId())).thenReturn(payment);
            Assertions.assertThrows(AlreadyExistException.class, () -> {
                memberCommandService.addPaymentByMemberId(paymentRequest, member.getId());
            });
        }
    }

    @Nested
    public class DeletePaymentById {
        @Test
        public void shouldDeletePayment() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("test")
                    .build();
            Payment payment = Payment.builder()
                    .method(PaymentMethod.CARD)
                    .cardNumber("1234-1234-1234-1234")
                    .cardExpirationDate("12/34")
                    .member(member)
                    .build();

            // when
            when(paymentRepository.findOneById(payment.getId())).thenReturn(payment);
            doNothing().when(paymentRepository).deleteById(payment.getId());
            memberCommandService.deletePaymentById(payment.getId());

            // then
            Mockito.verify(paymentRepository, Mockito.times(1)).deleteById(payment.getId());
        }

        @Test
        public void shouldNotDeletePayment() {
            // given & when
            when(paymentRepository.findOneById(any())).thenReturn(null);
            Assertions.assertThrows(NotFoundPaymentException.class, () -> {
                memberCommandService.deletePaymentById(UUID.randomUUID());
            });
        }
    }

    @Nested
    public class ValidatePasswordByMemberId {
        @Test
        public void shouldValidatePassword() {
            // given
            String password = "test";
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password(password)
                    .build();

            // when
            when(memberRepository.findOneById(member.getId())).thenReturn(member);
            memberCommandService.validatePasswordByMemberId(password, member.getId());
        }

        @Test
        public void shouldThrowUnauthorizedPasswordException() {
            // given
            String password = "test";
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password(password)
                    .build();

            // when
            when(memberRepository.findOneById(member.getId())).thenReturn(member);
            Assertions.assertThrows(UnauthorizedPasswordException.class, () -> {
                memberCommandService.validatePasswordByMemberId("wrong password", member.getId());
            });
        }
    }

    @Nested
    class DeleteMemberById {
        @Test
        public void shouldDeleteMemberById() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("password")
                    .build();

            // when
            when(memberRepository.findOneById(member.getId())).thenReturn(member);
            doNothing().when(memberRepository).deleteById(member.getId());
            memberCommandService.deleteMemberById(member.getId());

            // then
            Mockito.verify(memberRepository, Mockito.times(1)).deleteById(member.getId());
        }
    }
}