package com.mozza.springboilerplate.service.query.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class PaymentQueryServiceImplTest {
    @MockBean
    private MemberRepositoryCustomImpl memberRepositoryCustom;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PaymentRepositoryCustomImpl paymentRepositoryCustom;

    @MockBean
    private PaymentRepository paymentRepository;

    private PaymentQueryServiceImpl paymentQueryService;

    @BeforeEach
    public void beforeEach() {
        paymentQueryService = new PaymentQueryServiceImpl(paymentRepository);
    }

    @Nested
    public class GetById {
        @Test
        public void shouldGetPaymentById() {
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
                    .member(member)
                    .cardNumber("1234-1234-1234-1234")
                    .cardExpirationDate("12/34")
                    .build();
            when(paymentRepository.findOneById(payment.getId())).thenReturn(payment);

            // when
            PaymentResult paymentResult = paymentQueryService.getById(payment.getId());

            // then
            Assertions.assertEquals(paymentResult.getId(), payment.getId());
        }

        @Test
        public void shouldNullPaymentById() {
            // given & when
            when(paymentRepository.findOneById(any())).thenReturn(null);
            PaymentResult paymentResult = paymentQueryService.getById(UUID.randomUUID());

            // then
            Assertions.assertNull(paymentResult);

        }
    }

    @Nested
    public class GetAllByMemberId {
        @Test
        public void shouldGetAllPaymentsByMemberId() {
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
                    .member(member)
                    .cardNumber("1234-1234-1234-1234")
                    .cardExpirationDate("12/34")
                    .build();
            when(paymentRepository.findAllByMemberId(member.getId())).thenReturn(List.of(payment));

            // when
            List<PaymentResult> paymentResults = paymentQueryService.getAllByMemberId(member.getId());

            // then
            Assertions.assertEquals(paymentResults.size(), 1);
        }

        @Test
        public void shouldEmptyPaymentsByMemberId() {
            // given & when
            when(paymentRepository.findAllByMemberId(any())).thenReturn(List.of());
            List<PaymentResult> paymentResults = paymentQueryService.getAllByMemberId(UUID.randomUUID());

            // then
            Assertions.assertNull(paymentResults);
        }
    }

    @Nested
    public class GetCountByMemberId {
        @Test
        public void shouldGetCountByMemberId() {
            // given
            Member member = Member.builder()
                    .name("test")
                    .email("test@dev.com")
                    .role(MemberRole.USER)
                    .phoneNumber("000-0000-0000")
                    .password("test")
                    .build();
            when(paymentRepository.countByMemberId(member.getId())).thenReturn(1L);

            // when
            Long count = paymentQueryService.getCountByMemberId(member.getId());

            // then
            Assertions.assertEquals(count, 1L);
        }

        @Test
        public void shouldZeroCountByMemberId() {
            // given & when
            when(paymentRepository.countByMemberId(any())).thenReturn(0L);
            Long count = paymentQueryService.getCountByMemberId(UUID.randomUUID());

            // then
            Assertions.assertEquals(count, 0L);
        }
    }
}
