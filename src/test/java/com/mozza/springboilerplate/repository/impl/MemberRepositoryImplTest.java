package com.mozza.springboilerplate.repository.impl;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.config.QuerydslConfig;
import com.mozza.springboilerplate.domain.member.dto.MemberCond;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.util.UUID;

@DataJpaTest
@Import({QuerydslConfig.class})
public class MemberRepositoryImplTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void beforeEach() {
        Member member = Member.builder()
                .name("test")
                .role(MemberRole.USER)
                .email("example@dev.com")
                .phoneNumber("000-0000-0000")
                .password("test")
                .build();
        this.member = memberRepository.save(member);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    public void shouldFindMemberByEmail() {
        // given
        String email = "example@dev.com";

        // when
        Member member = memberRepository.findOneByEmail(email);

        // then
        Assertions.assertEquals(member.getEmail(), email);
    }

    @Test
    public void shouldNotFoundMemberByEmail() {
        // given
        String email = "test@dev.com";

        // when
        Member member = memberRepository.findOneByEmail(email);

        // then
        Assertions.assertNull(member);
    }

    @Test
    public void shouldFoundMemberById() {
        // given
        UUID id = this.member.getId();

        // when
        Member member = memberRepository.findOneById(id);

        // then
        Assertions.assertEquals(member.getId(), id);
    }

    @Test
    public void shouldNotFoundMemberById() {
        // given
        UUID id = UUID.randomUUID();

        // when
        Member member = memberRepository.findOneById(id);

        // then
        Assertions.assertNull(member);
    }

    @Test
    public void shouldFindAllMembers() {
        // given
        MemberCond cond = new MemberCond();

        // when
        Page<MemberResult> members = memberRepository.findAll(cond);

        // then
        Assertions.assertEquals(members.getTotalElements(), 1);
        Assertions.assertEquals(members.getTotalPages(), 1);
    }

    @Test
    public void shouldNotFoundMembers() {
        // given
        MemberCond cond = new MemberCond()
                .setEmail("notUser@dev.com");

        // when
        Page<MemberResult> members = memberRepository.findAll(cond);

        // then
        Assertions.assertEquals(members.getTotalElements(), 0);
        Assertions.assertTrue(members.isEmpty());
    }

    @Test
    public void shouldFindMembersByEmail() {
        // given
        MemberCond cond = new MemberCond()
                .setEmail("example@dev.com");

        // when
        Page<MemberResult> members = memberRepository.findAll(cond);

        // then
        Assertions.assertEquals(members.getTotalElements(), 1);
    }

    @Test
    public void shouldFindMemberByPhoneNumber() {
        // given
        MemberCond cond = new MemberCond()
                .setPhoneNumber("000-0000-0000");

        // when
        Page<MemberResult> members = memberRepository.findAll(cond);

        // then
        Assertions.assertEquals(members.getTotalElements(), 1);
    }

    @Test
    public void shouldFindMemberByName() {
        // given
        MemberCond cond = new MemberCond()
                .setName("test");

        // when
        Page<MemberResult> members = memberRepository.findAll(cond);

        // then
        Assertions.assertEquals(members.getTotalElements(), 1);
    }
}
