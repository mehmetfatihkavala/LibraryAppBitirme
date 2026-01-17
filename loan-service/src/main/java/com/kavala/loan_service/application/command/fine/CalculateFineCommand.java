package com.kavala.loan_service.application.command.fine;

import com.kavala.loan_service.core.cqrs.Command;
import com.kavala.loan_service.domain.model.FineAmount;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to calculate fine for a loan.
 */
public class CalculateFineCommand implements Command<FineAmount> {

    private final UUID loanId;

    private CalculateFineCommand(UUID loanId) {
        this.loanId = Objects.requireNonNull(loanId, "LoanId cannot be null");
    }

    public static CalculateFineCommand of(UUID loanId) {
        return new CalculateFineCommand(loanId);
    }

    public UUID getLoanId() {
        return loanId;
    }

    @Override
    public String toString() {
        return String.format("CalculateFineCommand{loanId=%s}", loanId);
    }
}
