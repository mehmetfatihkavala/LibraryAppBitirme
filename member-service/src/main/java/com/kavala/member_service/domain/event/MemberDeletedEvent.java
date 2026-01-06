package com.kavala.member_service.domain.event;

import com.kavala.member_service.domain.model.MemberId;

public record MemberDeletedEvent(MemberId memberId) {

}
