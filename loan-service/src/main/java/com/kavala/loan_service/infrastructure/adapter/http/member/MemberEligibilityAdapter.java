package com.kavala.loan_service.infrastructure.adapter.http.member;

import com.kavala.loan_service.domain.model.MemberId;
import com.kavala.loan_service.domain.port.MemberEligibilityPort;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Adapter implementing MemberEligibilityPort using HTTP client.
 */
@Component
public class MemberEligibilityAdapter implements MemberEligibilityPort {

    private final MemberClient memberClient;

    public MemberEligibilityAdapter(MemberClient memberClient) {
        this.memberClient = Objects.requireNonNull(memberClient, "MemberClient cannot be null");
    }

    @Override
    public boolean memberExists(MemberId memberId) {
        try {
            return memberClient.memberExists(memberId.getValue());
        } catch (Exception e) {
            // Log and return false on error
            return false;
        }
    }

    @Override
    public boolean canBorrow(MemberId memberId) {
        try {
            return memberClient.canBorrow(memberId.getValue());
        } catch (Exception e) {
            // Log and return false on error
            return false;
        }
    }

    @Override
    public void validateEligibility(MemberId memberId) {
        if (!memberExists(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
        if (!canBorrow(memberId)) {
            throw new MemberNotEligibleException(memberId, "Member is not eligible to borrow");
        }
    }
}
