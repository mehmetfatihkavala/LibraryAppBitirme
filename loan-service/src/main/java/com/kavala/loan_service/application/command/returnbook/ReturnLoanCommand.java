package com.kavala.loan_service.application.command.returnbook;

import com.kavala.loan_service.core.cqrs.Command;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to return a book (close a loan).
 */
public class ReturnLoanCommand implements Command<Void> {

    private final UUID loanId;

    private ReturnLoanCommand(UUID loanId) {
        this.loanId = Objects.requireNonNull(loanId, "LoanId cannot be null");
    }

    public static ReturnLoanCommand of(UUID loanId) {
        return new ReturnLoanCommand(loanId);
    }

    public UUID getLoanId() {
        return loanId;
    }

    @Override
    public String toString() {
        return String.format("ReturnLoanCommand{loanId=%s}", loanId);
    }
}
