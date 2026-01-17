package com.kavala.loan_service.application.query.list;

import com.kavala.loan_service.core.cqrs.Query;
import com.kavala.loan_service.domain.model.Loan;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Query to list all loans for a member.
 */
public class ListLoansByMemberQuery implements Query<List<Loan>> {

    private final UUID memberId;

    private ListLoansByMemberQuery(UUID memberId) {
        this.memberId = Objects.requireNonNull(memberId, "MemberId cannot be null");
    }

    public static ListLoansByMemberQuery of(UUID memberId) {
        return new ListLoansByMemberQuery(memberId);
    }

    public UUID getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return String.format("ListLoansByMemberQuery{memberId=%s}", memberId);
    }
}
