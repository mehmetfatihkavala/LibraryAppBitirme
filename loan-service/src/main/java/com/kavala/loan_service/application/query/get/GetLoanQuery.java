package com.kavala.loan_service.application.query.get;

import com.kavala.loan_service.core.cqrs.Query;
import com.kavala.loan_service.domain.model.Loan;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Query to get a single loan by ID.
 */
public class GetLoanQuery implements Query<Optional<Loan>> {

    private final UUID loanId;

    private GetLoanQuery(UUID loanId) {
        this.loanId = Objects.requireNonNull(loanId, "LoanId cannot be null");
    }

    public static GetLoanQuery of(UUID loanId) {
        return new GetLoanQuery(loanId);
    }

    public UUID getLoanId() {
        return loanId;
    }

    @Override
    public String toString() {
        return String.format("GetLoanQuery{loanId=%s}", loanId);
    }
}
