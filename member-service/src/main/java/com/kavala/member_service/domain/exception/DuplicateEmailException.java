package com.kavala.member_service.domain.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Member already exists with email: " + email);
    }
}
