package com.kavala.member_service.domain.event;

import java.time.Instant;

import com.kavala.member_service.domain.model.MemberId;
import com.kavala.member_service.domain.model.Name;

public record MemberCreatedEvent(MemberId memberId, Name name, String email, String phone, Instant createdAt) {

}
