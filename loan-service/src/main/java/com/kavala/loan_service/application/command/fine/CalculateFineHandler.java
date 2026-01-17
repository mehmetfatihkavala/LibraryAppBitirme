package com.kavala.loan_service.application.command.fine;

import com.kavala.loan_service.core.cqrs.CommandHandler;
import com.kavala.loan_service.domain.model.FineAmount;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.port.EventPublisher;
import com.kavala.loan_service.domain.port.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Handler for CalculateFineCommand.
 * Implements the use case of calculating fine for a loan.
 */
@Service
@Transactional
public class CalculateFineHandler implements CommandHandler<CalculateFineCommand, FineAmount> {

    private final LoanRepository loanRepository;
    private final EventPublisher eventPublisher;

    public CalculateFineHandler(
            LoanRepository loanRepository,
            EventPublisher eventPublisher) {
        this.loanRepository = Objects.requireNonNull(loanRepository, "LoanRepository cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "EventPublisher cannot be null");
    }

    @Override
    public FineAmount handle(CalculateFineCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        LoanId loanId = LoanId.of(command.getLoanId());

        // Find the loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        // Calculate fine
        FineAmount fineAmount = loan.calculateFine();

        // Persist changes
        loanRepository.save(loan);

        // Publish domain events
        eventPublisher.publishAll(loan.pullDomainEvents());

        return fineAmount;
    }

    /**
     * Exception thrown when loan is not found.
     */
    public static class LoanNotFoundException extends RuntimeException {
        private final LoanId loanId;

        public LoanNotFoundException(LoanId loanId) {
            super(String.format("Loan not found: %s", loanId));
            this.loanId = loanId;
        }

        public LoanId getLoanId() {
            return loanId;
        }
    }
}
