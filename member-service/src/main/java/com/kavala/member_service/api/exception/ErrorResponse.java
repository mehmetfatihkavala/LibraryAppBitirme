package com.kavala.member_service.api.exception;

import java.time.Instant;

public record ErrorResponse(int status,
        String message,
        Instant timestamp) {

}
