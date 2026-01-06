package com.kavala.member_service.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.kavala.member_service.domain.model.Member;
import com.kavala.member_service.domain.model.MemberId;
import com.kavala.member_service.domain.model.Name;

@Component
public class MemberMapper {

    public MemberEntity toEntity(Member member) {
        return new MemberEntity(
                member.getId() != null ? member.getId().value() : null,
                member.getName().firstName(),
                member.getName().lastName(),
                member.getEmail(),
                member.getPhone(),
                member.getStatus());
    }

    public Member toDomain(MemberEntity entity) {
        return new Member(
                new MemberId(entity.getId()),
                new Name(entity.getFirstName(), entity.getLastName()),
                entity.getEmail(),
                entity.getPhone(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
