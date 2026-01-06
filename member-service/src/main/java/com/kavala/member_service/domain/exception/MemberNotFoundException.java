package com.kavala.member_service.domain.exception;

import com.kavala.member_service.domain.model.MemberId;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(MemberId id) {
        super("Member not found with id: " + id.value());
    }

}
