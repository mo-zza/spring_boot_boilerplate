package com.mozza.springboilerplate.service.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.member.exception.AlreadyExistException;
import com.mozza.springboilerplate.domain.member.exception.NotFoundMemberException;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;
import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.dto.PaymentsWithCountResponse;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.domain.payment.exception.NotFoundPaymentException;
import com.mozza.springboilerplate.domain.payment.exception.UnauthorizedPaymentException;
import com.mozza.springboilerplate.repository.impl.MemberRepositoryCustomImpl;
import com.mozza.springboilerplate.repository.impl.PaymentRepositoryCustomImpl;
import com.mozza.springboilerplate.service.command.MemberCommandService;
import com.mozza.springboilerplate.service.query.MemberQueryService;
import com.mozza.springboilerplate.service.query.PaymentQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class MemberServiceImplTest {
    @MockBean
    private MemberRepositoryCustomImpl memberRepositoryCustom;

    @MockBean
    private PaymentRepositoryCustomImpl paymentRepositoryCustom;

    @MockBean
    private MemberCommandService memberCommandService;

    @MockBean
    private MemberQueryService memberQueryService;

    @MockBean
    private PaymentQueryService paymentQueryService;

    private MemberServiceImpl memberService;

    @BeforeEach
    public void beforeEach() {
        memberService = new MemberServiceImpl(memberCommandService, memberQueryService, paymentQueryService);
    }

    @Nested
    public class GetMemberById {
        @Test
        public void shouldGetMemberById() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("password")
                    .build();
            MemberResult result = MemberResult.fromEntity(member);

            // when
            when(memberQueryService.getById(member.getId())).thenReturn(result);
            MemberResponse response = memberService.getMemberById(member.getId());

            // then
            Assertions.assertEquals(response.getName(), member.getName());
        }

        @Test
        public void shouldNullMemberById() {
            // given & when
            when(memberQueryService.getById(UUID.randomUUID())).thenReturn(null);
            MemberResponse response = memberService.getMemberById(null);

            // then
            Assertions.assertNull(response);
        }
    }

    @Nested
    public class GetPaymentWithCountByMemberId {
        @Test
        public void shouldGetPaymentWithCountByMemberId() {
            // given
            Member member = getMemberEntity();
            Payment payment = getPaymentEntity(member);
            PaymentResult paymentResult = PaymentResult.fromEntity(payment);

            // when
            when(paymentQueryService.getAllByMemberId(member.getId())).thenReturn(List.of(paymentResult));
            when(paymentQueryService.getCountByMemberId(member.getId())).thenReturn(1L);
            PaymentsWithCountResponse response = memberService.getPaymentWithCountByMemberId(member.getId());

            // then
            Assertions.assertEquals(response.getPayments().get(0).getCardNumber(), payment.getCardNumber());
        }

        @Test
        public void shouldBeZero() {
            // given & when
            when(paymentQueryService.getAllByMemberId(UUID.randomUUID())).thenReturn(List.of());
            when(paymentQueryService.getCountByMemberId(UUID.randomUUID())).thenReturn(0L);

            // then
            Assertions.assertEquals(memberService.getPaymentWithCountByMemberId(UUID.randomUUID()).getPayments().size(), 0);
        }
    }

    @Nested
    public class AddPaymentByMemberId {
        @Test
        public void shouldAddPaymentByMemberId() {
            // given
            Member member = getMemberEntity();
            Payment payment = getPaymentEntity(member);
            PaymentRequest paymentRequest = getCardPaymentRequest(payment.getCardNumber(), payment.getCardExpirationDate());
            PaymentResult paymentResult = PaymentResult.fromEntity(payment);

            // when
            when(paymentQueryService.getAllByMemberId(member.getId())).thenReturn(List.of(paymentResult));
            when(paymentQueryService.getCountByMemberId(member.getId())).thenReturn(1L);
            PaymentsWithCountResponse response = memberService.addPaymentByMemberId(paymentRequest, member.getId());

            // then
            Assertions.assertEquals(response.getPayments().get(0).getCardNumber(), payment.getCardNumber());
            verify(memberCommandService, times(1)).addPaymentByMemberId(paymentRequest, member.getId());
        }

        @Test
        public void shouldThrowAlreadyExistException() {
            // given
            Member member = getMemberEntity();
            Payment payment = getPaymentEntity(member);
            PaymentRequest paymentRequest = getCardPaymentRequest(payment.getCardNumber(), payment.getCardExpirationDate());

            // when
            doThrow(AlreadyExistException.class).when(memberCommandService).addPaymentByMemberId(paymentRequest, member.getId());
            Assertions.assertThrows(AlreadyExistException.class, () -> {
                memberService.addPaymentByMemberId(paymentRequest, member.getId());
            });
        }

        @Test
        public void shouldThrowNotFoundException() {
            // given
            UUID memberId = UUID.randomUUID();
            String cardNumber = "1234-1234-1234-1234";
            String cardExpirationDate = "12/34";
            PaymentRequest paymentRequest = getCardPaymentRequest(cardNumber, cardExpirationDate);

            // when
            doThrow(NotFoundMemberException.class).when(memberCommandService).addPaymentByMemberId(paymentRequest, memberId);
            Assertions.assertThrows(NotFoundMemberException.class, () -> {
                memberService.addPaymentByMemberId(paymentRequest, memberId);
            });
        }
    }

    @Nested
    public class DeletePaymentById {
        @Test
        public void shouldDeletePaymentById() {
            // given
            Member member = getMemberEntity();
            Payment payment = getPaymentEntity(member);
            PaymentResult paymentResult = PaymentResult.fromEntity(payment);
            UUID paymentId = payment.getId();
            UUID memberId = member.getId();

            // when
            when(paymentQueryService.getById(paymentId)).thenReturn(paymentResult);
            doNothing().when(memberCommandService).deleteMemberById(memberId);
            when(paymentQueryService.getAllByMemberId(memberId)).thenReturn(List.of());
            when(paymentQueryService.getCountByMemberId(memberId)).thenReturn(0L);
            PaymentsWithCountResponse response = memberService.deletePaymentByMemberId(paymentId, memberId);

            // then
            verify(memberCommandService, times(1)).deletePaymentById(paymentId);
            Assertions.assertEquals(response.getPayments().size(), 0);
        }

        @Test
        public void shouldNotAuthorizedPaymentException() {
            // given
            Member member = getMemberEntity();
            Payment payment = getPaymentEntity(member);
            PaymentResult paymentResult = PaymentResult.fromEntity(payment);
            UUID paymentId = payment.getId();
            UUID notMemberId = UUID.randomUUID();

            // when
            when(paymentQueryService.getById(paymentId)).thenReturn(paymentResult);
            Assertions.assertThrows(UnauthorizedPaymentException.class, () -> {
                memberService.deletePaymentByMemberId(paymentId, notMemberId);
            });
        }

        @Test
        public void shouldNotFoundException() {
            // given
            UUID paymentId = UUID.randomUUID();

            // when
            when(paymentQueryService.getById(paymentId)).thenReturn(null);
            Assertions.assertThrows(NotFoundPaymentException.class, () -> {
                memberService.deletePaymentByMemberId(paymentId, UUID.randomUUID());
            });
        }
    }

    @Nested
    public class DeleteMemberById {
        @Test
        public void shouldDeleteMemberById() {
            // given
            UUID memberId = UUID.randomUUID();

            // when
            doNothing().when(memberCommandService).deleteMemberById(memberId);
            memberService.deleteMemberById(memberId);

            // then
            verify(memberCommandService, times(1)).deleteMemberById(memberId);
        }
    }

    private Member getMemberEntity() {
        return Member.builder()
                .name("test")
                .email("test@dev.com")
                .role(MemberRole.USER)
                .phoneNumber("000-0000-0000")
                .password("password")
                .build();
    }

    private Payment getPaymentEntity(Member member) {
        return Payment.builder()
                .method(PaymentMethod.CARD)
                .member(member)
                .cardNumber("1234-1234-1234-1234")
                .cardExpirationDate("12/34")
                .build();
    }

    private PaymentRequest getCardPaymentRequest(String cardNumber, String cardExpirationDate) {
        return new PaymentRequest()
                .setMethod(PaymentMethod.CARD)
                .setCardNumber(cardNumber)
                .setCardExpirationDate(cardExpirationDate);
    }
}
