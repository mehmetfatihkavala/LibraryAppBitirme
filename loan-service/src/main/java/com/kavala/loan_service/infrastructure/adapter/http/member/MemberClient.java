package com.kavala.loan_service.infrastructure.adapter.http.member;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

/**
 * HTTP client for member-service.
 */
@HttpExchange("/api/members")
public interface MemberClient {

    /**
     * Checks if a member exists.
     */
    @GetExchange("/{memberId}/exists")
    boolean memberExists(@PathVariable UUID memberId);

    /**
     * Checks if a member can borrow books.
     */
    @GetExchange("/{memberId}/can-borrow")
    boolean canBorrow(@PathVariable UUID memberId);
}
