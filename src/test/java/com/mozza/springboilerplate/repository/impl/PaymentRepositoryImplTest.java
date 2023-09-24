package com.mozza.springboilerplate.repository.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.config.QuerydslConfig;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.repository.MemberRepository;
import com.mozza.springboilerplate.repository.PaymentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

@DataJpaTest
@Import({QuerydslConfig.class})
public class PaymentRepositoryImplTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Payment payment;

    private Member member;

    @BeforeEach
    public void beforeEach() {
        Member member = Member.builder()
                .name("payment_member")
                .email("payment_member@test.com")
                .phoneNumber("010-1234-1234")
                .role(MemberRole.USER)
                .password("password")
                .build();
        Payment payment = Payment.builder()
                .method(PaymentMethod.CARD)
                .cardNumber("1234-1234-1234-1234")
                .cardExpirationDate("12/34")
                .member(member)
                .build();
        member.addPayment(payment);

        this.member = memberRepository.save(member);
        this.payment = this.member.getPayments().stream()
                .toList().get(0);
    }

    @AfterEach
    public void afterEach() {
        paymentRepository.deleteAll();
    }

    @Nested
    class FindPaymentById {
        @Test
        public void shouldFindPaymentById() {
            // given
            UUID id = payment.getId();

            // when
            PaymentResult payment = paymentRepository.findOneById(id);

            // then
            Assertions.assertEquals(payment.getId(), id);
        }

        @Test
        public void shouldNullPaymentById() {
            // given
            UUID id = UUID.randomUUID();

            // when
            PaymentResult payment = paymentRepository.findOneById(id);

            // then
            Assertions.assertNull(payment);
        }
    }

    @Nested
    class FindPaymentByCardNumberAndMemberId {
        @Test
        public void shouldFindPaymentByCardNumberAndMemberId() {
            // given
            String cardNumber = payment.getCardNumber();

            // when
            Payment payment = paymentRepository.findOneByCardNumberAndMemberId(cardNumber, member.getId());

            // then
            Assertions.assertEquals(payment.getCardNumber(), cardNumber);
            Assertions.assertEquals(payment.getMember().getId(), member.getId());
        }

        @Test
        public void shouldNullPaymentByCardNumberAndMemberId() {
            // given
            String cardNumber = "1234-1234-1234-1235";

            // when
            Payment payment = paymentRepository.findOneByCardNumberAndMemberId(cardNumber, member.getId());

            // then
            Assertions.assertNull(payment);
        }
    }

    @Nested
    class FindPaymentByMemberId {
        @Test
        public void shouldFindPaymentByMemberId() {
            // given
            UUID memberId = member.getId();

            // when
            List<PaymentResult> payment = paymentRepository.findAllByMemberId(memberId);

            // then
            Assertions.assertTrue(payment.stream().anyMatch(paymentResult -> paymentResult.getMember().getId().equals(memberId)));
        }

        @Test
        public void shouldZeroPaymentByMemberId() {
            // given
            UUID memberId = UUID.randomUUID();

            // when
            List<PaymentResult> payment = paymentRepository.findAllByMemberId(memberId);

            // then
            Assertions.assertEquals(payment.size(), 0L);
        }
    }

    @Nested
    class CountPaymentByMemberId {
        @Test
        public void shouldCountPaymentByMemberId() {
            // given
            UUID memberId = member.getId();

            // when
            Long count = paymentRepository.countByMemberId(memberId);

            // then
            Assertions.assertEquals(count, 1L);
        }

        @Test
        public void shouldZeroPaymentByMemberId() {
            // given
            UUID memberId = UUID.randomUUID();

            // when
            Long count = paymentRepository.countByMemberId(memberId);

            // then
            Assertions.assertEquals(count, 0L);
        }
    }
}
