package com.kavala.member_service.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.kavala.member_service.domain.model.Member;
import com.kavala.member_service.domain.model.MemberId;
import com.kavala.member_service.domain.model.Name;

@Component
public class MemberMapper {

    public MemberEntity toEntity(Member member) {
        MemberEntity entity = new MemberEntity(
                member.getId() != null ? member.getId().value() : null,
                member.getName().firstName(),
                member.getName().lastName(),
                member.getEmail(),
                member.getPhone(),
                member.getStatus());

        // ID varsa set et (update durumları için)
        if (member.getId() != null) {
            entity.setId(member.getId().value());
        }

        // Timestamp'leri manuel set et
        if (member.getCreatedAt() != null) {
            entity.setCreatedAt(member.getCreatedAt());
        }
        if (member.getUpdatedAt() != null) {
            entity.setUpdatedAt(member.getUpdatedAt());
        }

        return entity;
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
