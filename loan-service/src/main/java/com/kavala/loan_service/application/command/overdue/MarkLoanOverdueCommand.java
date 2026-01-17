package com.kavala.loan_service.application.command.overdue;

import com.kavala.loan_service.core.cqrs.Command;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to mark a loan as overdue.
 */
public class MarkLoanOverdueCommand implements Command<Void> {

    private final UUID loanId;

    private MarkLoanOverdueCommand(UUID loanId) {
        this.loanId = Objects.requireNonNull(loanId, "LoanId cannot be null");
    }

    public static MarkLoanOverdueCommand of(UUID loanId) {
        return new MarkLoanOverdueCommand(loanId);
    }

    public UUID getLoanId() {
        return loanId;
    }

    @Override
    public String toString() {
        return String.format("MarkLoanOverdueCommand{loanId=%s}", loanId);
    }
}
