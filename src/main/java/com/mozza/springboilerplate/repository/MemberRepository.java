package com.mozza.springboilerplate.repository;

import com.mozza.springboilerplate.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID>, MemberRepositoryCustom {
}
