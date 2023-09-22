package com.mozza.springboilerplate.repository.impl;

import com.mozza.springboilerplate.domain.member.dto.MemberCond;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.dto.QMemberResult;
import com.mozza.springboilerplate.domain.member.entity.Member;
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
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public Member findOneByEmail(String email) {
        return getMemberQuery()
                .where(member.email.eq(email))
                .fetchOne();
    }

    @Override
    public Member findOneById(UUID id) {
        return getMemberQuery()
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public Page<MemberResult> findAll(MemberCond cond) {
        List<MemberResult> content = getMemberQueryWithDto()
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

    private JPAQuery<MemberResult> getMemberQueryWithDto() {
        return query.select(new QMemberResult(
                        member.id,
                        member.name,
                        member.role,
                        member.email,
                        member.phoneNumber,
                        member.payments,
                        member.encryptedPassword,
                        member.salt,
                        member.createdDate,
                        member.modifiedDate
                ))
                .from(member)
                .leftJoin(member.payments);
    }

    private JPAQuery<Member> getMemberQuery() {
        return query.selectFrom(member)
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
