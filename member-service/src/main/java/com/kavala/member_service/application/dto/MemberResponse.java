package com.kavala.member_service.application.dto;

import java.util.UUID;

import com.kavala.member_service.domain.model.Member;

public record MemberResponse(UUID id, String firstName, String lastName, String fullName, String email, String phone,
        String status,
        String createdAt, String updatedAt) {

    public static MemberResponse fromMember(Member member) {
        return new MemberResponse(member.getId().value(), member.getName().firstName(),
                member.getName().lastName(), member.getName().getFullName(), member.getEmail(), member.getPhone(),
                member.getStatus().name(), member.getCreatedAt().toString(), member.getUpdatedAt().toString());
    }

}
