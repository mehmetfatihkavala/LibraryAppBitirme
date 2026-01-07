package com.kavala.member_service.domain.port;

import java.util.List;
import java.util.Optional;

import com.kavala.member_service.domain.model.Member;
import com.kavala.member_service.domain.model.MemberId;
import com.kavala.member_service.domain.model.MemberStatus;

public interface MemberRepository {
    Member save(Member member);

    Member update(Member member);

    void deleteById(MemberId id);

    Optional<Member> findById(MemberId id);

    List<Member> findAll();

    boolean existsByEmail(String email);

    List<Member> findByStatus(MemberStatus status);

}
