package com.kavala.loan_service.application.command.checkout;

import com.kavala.loan_service.core.cqrs.Command;
import com.kavala.loan_service.domain.model.LoanId;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to checkout a book (create a new loan).
 * Part of CQRS command pattern for loan management.
 */
public class CheckoutLoanCommand implements Command<LoanId> {

    private final UUID memberId;
    private final UUID bookCopyId;
    private final int loanDays;

    private CheckoutLoanCommand(Builder builder) {
        this.memberId = Objects.requireNonNull(builder.memberId, "MemberId cannot be null");
        this.bookCopyId = Objects.requireNonNull(builder.bookCopyId, "BookCopyId cannot be null");
        this.loanDays = builder.loanDays > 0 ? builder.loanDays : 14; // Default 14 days
    }

    public UUID getMemberId() {
        return memberId;
    }

    public UUID getBookCopyId() {
        return bookCopyId;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID memberId;
        private UUID bookCopyId;
        private int loanDays = 14;

        public Builder memberId(UUID memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder bookCopyId(UUID bookCopyId) {
            this.bookCopyId = bookCopyId;
            return this;
        }

        public Builder loanDays(int loanDays) {
            this.loanDays = loanDays;
            return this;
        }

        public CheckoutLoanCommand build() {
            return new CheckoutLoanCommand(this);
        }
    }

    @Override
    public String toString() {
        return String.format("CheckoutLoanCommand{memberId=%s, bookCopyId=%s, loanDays=%d}",
                memberId, bookCopyId, loanDays);
    }
}
