package com.kavala.member_service.domain.port;

import java.util.List;
import java.util.Optional;

import com.kavala.member_service.domain.model.Member;
import com.kavala.member_service.domain.model.MemberId;

public interface MemberRepository {
    Member save(Member member);

    Member update(Member member);

    void delete(MemberId id);

    void block(MemberId id);

    void unblock(MemberId id);

    Optional<Member> findById(MemberId id);

    List<Member> findAllBlocked();

    List<Member> findAllActive();

    List<Member> findAll();

    boolean existsByEmail(String email);

}
