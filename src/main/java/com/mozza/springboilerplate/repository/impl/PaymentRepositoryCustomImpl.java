package com.mozza.springboilerplate.repository.impl;

import com.mozza.springboilerplate.domain.member.entity.QMember;
import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.dto.QPaymentResult;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.repository.PaymentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.mozza.springboilerplate.domain.payment.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public PaymentResult findOneById(UUID id) {
        return getPaymentQueryWithDto()
                .where(payment.id.eq(id))
                .fetchOne();
    }

    @Override
    public Payment findOneByCardNumberAndMemberId(String cardNumber, UUID memberId) {
        return getPaymentQuery()
                .where(payment.cardNumber.eq(cardNumber), payment.member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public List<PaymentResult> findAllByMemberId(UUID memberId) {
        return getPaymentQueryWithDto()
                .where(payment.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public Long countByMemberId(UUID memberId) {
        return query.select(payment.count())
                .from(payment)
                .where(payment.member.id.eq(memberId))
                .fetchOne();
    }

    private JPAQuery<PaymentResult> getPaymentQueryWithDto() {
        return query.select(new QPaymentResult(
                        payment.id,
                        payment.member,
                        payment.method,
                        payment.cardNumber,
                        payment.cardExpirationDate,
                        payment.createdDate,
                        payment.modifiedDate
                ))
                .from(payment)
                .innerJoin(QMember.member).fetchJoin();
    }

    private JPAQuery<Payment> getPaymentQuery() {
        return query.selectFrom(payment)
                .leftJoin(payment.member).fetchJoin();
    }
}
