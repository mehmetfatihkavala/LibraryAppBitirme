package com.kavala.loan_service.domain.port;

import com.kavala.loan_service.domain.model.MemberId;

/**
 * Driven port for checking member eligibility.
 * This port abstracts the communication with member-service.
 * 
 * Following Hexagonal Architecture:
 * - Domain layer defines this interface
 * - Infrastructure layer provides HTTP client implementation
 */
public interface MemberEligibilityPort {

    /**
     * Checks if a member exists in the system.
     *
     * @param memberId the member ID to check
     * @return true if member exists
     */
    boolean memberExists(MemberId memberId);

    /**
     * Checks if a member is eligible to borrow books.
     * A member may be ineligible due to:
     * - Too many active loans
     * - Unpaid fines
     * - Suspended membership
     *
     * @param memberId the member ID to check
     * @return true if member can borrow
     */
    boolean canBorrow(MemberId memberId);

    /**
     * Validates that a member exists and is eligible.
     *
     * @param memberId the member ID to validate
     * @throws MemberNotFoundException    if member does not exist
     * @throws MemberNotEligibleException if member cannot borrow
     */
    void validateEligibility(MemberId memberId);

    /**
     * Exception thrown when member is not found.
     */
    class MemberNotFoundException extends RuntimeException {
        private final MemberId memberId;

        public MemberNotFoundException(MemberId memberId) {
            super(String.format("Member not found: %s", memberId));
            this.memberId = memberId;
        }

        public MemberId getMemberId() {
            return memberId;
        }
    }

    /**
     * Exception thrown when member is not eligible to borrow.
     */
    class MemberNotEligibleException extends RuntimeException {
        private final MemberId memberId;
        private final String reason;

        public MemberNotEligibleException(MemberId memberId, String reason) {
            super(String.format("Member %s is not eligible to borrow: %s", memberId, reason));
            this.memberId = memberId;
            this.reason = reason;
        }

        public MemberId getMemberId() {
            return memberId;
        }

        public String getReason() {
            return reason;
        }
    }
}
