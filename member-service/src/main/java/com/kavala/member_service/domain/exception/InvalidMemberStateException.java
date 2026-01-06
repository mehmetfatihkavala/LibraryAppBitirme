package com.kavala.member_service.domain.exception;

public class InvalidMemberStateException extends RuntimeException {

    public InvalidMemberStateException(String message) {
        super(message);
    }
}
