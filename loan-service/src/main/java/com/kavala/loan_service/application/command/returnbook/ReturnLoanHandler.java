package com.kavala.loan_service.application.command.returnbook;

import com.kavala.loan_service.core.cqrs.CommandHandler;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.port.EventPublisher;
import com.kavala.loan_service.domain.port.InventoryCopyPort;
import com.kavala.loan_service.domain.port.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Handler for ReturnLoanCommand.
 * Implements the use case of returning a book.
 */
@Service
@Transactional
public class ReturnLoanHandler implements CommandHandler<ReturnLoanCommand, Void> {

    private final LoanRepository loanRepository;
    private final InventoryCopyPort inventoryCopyPort;
    private final EventPublisher eventPublisher;

    public ReturnLoanHandler(
            LoanRepository loanRepository,
            InventoryCopyPort inventoryCopyPort,
            EventPublisher eventPublisher) {
        this.loanRepository = Objects.requireNonNull(loanRepository, "LoanRepository cannot be null");
        this.inventoryCopyPort = Objects.requireNonNull(inventoryCopyPort, "InventoryCopyPort cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "EventPublisher cannot be null");
    }

    @Override
    public Void handle(ReturnLoanCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        LoanId loanId = LoanId.of(command.getLoanId());

        // Find the loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        // Calculate fine if overdue
        loan.calculateFine();

        // Return the book
        loan.returnBook();

        // Persist changes
        loanRepository.save(loan);

        // Mark book copy as returned in inventory
        inventoryCopyPort.markAsReturned(loan.getBookCopyId());

        // Publish domain events
        eventPublisher.publishAll(loan.pullDomainEvents());

        return null;
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
