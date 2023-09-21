package com.mozza.springboilerplate.repository.impl;

import com.mozza.springboilerplate.domain.member.dto.MemberCond;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.domain.member.dto.QMemberResponse;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.entity.QPayment;
import com.mozza.springboilerplate.repository.MemberRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static com.mozza.springboilerplate.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public MemberResponse findOneByEmail(String email) {
        return getMemberQueryWithJoin()
                .where(member.email.eq(email))
                .fetchOne();
    }

    @Override
    public MemberResponse findOneByPhoneNumber(String phoneNumber) {
        return getMemberQueryWithJoin()
                .where(member.phoneNumber.eq(phoneNumber))
                .fetchOne();
    }

    @Override
    public MemberResponse findOneById(UUID id) {
        return getMemberQueryWithJoin()
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public Page<MemberResponse> findAll(MemberCond cond) {
        List<MemberResponse> content = getMemberQueryWithJoin()
                .where(nameContains(cond.getName()),
                        emailContains(cond.getEmail()),
                        phoneNumberContains(cond.getPhoneNumber()),
                        paymentMethodEq(cond.getPaymentMethod()))
                .offset(cond.getPage())
                .limit(cond.getSize())
                .fetch();
        Long count = query.select(member.count())
                .from(member)
                .where(nameContains(cond.getName()),
                        emailContains(cond.getEmail()),
                        phoneNumberContains(cond.getPhoneNumber()),
                        paymentMethodEq(cond.getPaymentMethod()))
                .fetchOne();
        return new PageImpl<>(content, PageRequest.of(cond.getPage(), cond.getSize()), count);
    }

    private JPAQuery<MemberResponse> getMemberQueryWithJoin() {
        return query.select(new QMemberResponse(
                        member.id,
                        member.name,
                        member.email,
                        member.phoneNumber,
                        member.payments,
                        member.createdDate,
                        member.modifiedDate
                ))
                .from(member)
                .leftJoin(member.payments);
    }

    private BooleanExpression nameContains(String name) {
        return name != null ? member.name.contains(name) : null;
    }

    private BooleanExpression emailContains(String email) {
        return email != null ? member.email.contains(email) : null;
    }

    private BooleanExpression phoneNumberContains(String phoneNumber) {
        return phoneNumber != null ? member.phoneNumber.contains(phoneNumber) : null;
    }

    private BooleanExpression paymentMethodEq(PaymentMethod paymentMethod) {
        return paymentMethod != null ? QPayment.payment.method.eq(paymentMethod) : null;
    }
}
