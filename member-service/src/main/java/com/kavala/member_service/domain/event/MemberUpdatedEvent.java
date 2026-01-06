package com.kavala.member_service.domain.event;

import java.time.Instant;

import com.kavala.member_service.domain.model.Name;
import com.kavala.member_service.domain.model.MemberId;

public record MemberUpdatedEvent(MemberId memberId, Name newName, String newEmail, String newPhone, Instant updatedAt) {

}
