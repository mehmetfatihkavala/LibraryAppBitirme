package com.kavala.member_service.application.dto;

import com.kavala.member_service.domain.model.MemberId;

public record UpdateMemberCommand(MemberId memberId, String firstName, String lastName, String email, String phone) {

}
