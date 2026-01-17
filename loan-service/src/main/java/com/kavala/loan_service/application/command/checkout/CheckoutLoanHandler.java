package com.kavala.loan_service.application.command.checkout;

import com.kavala.loan_service.core.cqrs.CommandHandler;
import com.kavala.loan_service.domain.model.BookCopyId;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.model.MemberId;
import com.kavala.loan_service.domain.port.EventPublisher;
import com.kavala.loan_service.domain.port.InventoryCopyPort;
import com.kavala.loan_service.domain.port.LoanRepository;
import com.kavala.loan_service.domain.port.MemberEligibilityPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Handler for CheckoutLoanCommand.
 * Implements the use case of creating a new loan (book checkout).
 * 
 * Follows CQRS pattern by separating command handling from queries.
 * Uses domain events for eventual consistency with other bounded contexts.
 */
@Service
@Transactional
public class CheckoutLoanHandler implements CommandHandler<CheckoutLoanCommand, LoanId> {

    private final LoanRepository loanRepository;
    private final MemberEligibilityPort memberEligibilityPort;
    private final InventoryCopyPort inventoryCopyPort;
    private final EventPublisher eventPublisher;

    public CheckoutLoanHandler(
            LoanRepository loanRepository,
            MemberEligibilityPort memberEligibilityPort,
            InventoryCopyPort inventoryCopyPort,
            EventPublisher eventPublisher) {
        this.loanRepository = Objects.requireNonNull(loanRepository, "LoanRepository cannot be null");
        this.memberEligibilityPort = Objects.requireNonNull(memberEligibilityPort,
                "MemberEligibilityPort cannot be null");
        this.inventoryCopyPort = Objects.requireNonNull(inventoryCopyPort, "InventoryCopyPort cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "EventPublisher cannot be null");
    }

    @Override
    public LoanId handle(CheckoutLoanCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        // Create value objects from command
        MemberId memberId = MemberId.of(command.getMemberId());
        BookCopyId bookCopyId = BookCopyId.of(command.getBookCopyId());

        // Validate member eligibility
        memberEligibilityPort.validateEligibility(memberId);

        // Validate book copy availability
        inventoryCopyPort.validateAvailability(bookCopyId);

        // Check if book is already on loan
        loanRepository.findActiveByBookCopyId(bookCopyId).ifPresent(existingLoan -> {
            throw new BookAlreadyOnLoanException(bookCopyId);
        });

        // Create the loan aggregate
        Loan loan = Loan.checkout(memberId, bookCopyId, command.getLoanDays());

        // Persist the loan
        Loan savedLoan = loanRepository.save(loan);

        // Mark book copy as loaned in inventory
        inventoryCopyPort.markAsLoaned(bookCopyId);

        // Publish domain events
        eventPublisher.publishAll(savedLoan.pullDomainEvents());

        return savedLoan.getId();
    }

    /**
     * Exception thrown when book is already on loan.
     */
    public static class BookAlreadyOnLoanException extends RuntimeException {
        private final BookCopyId bookCopyId;

        public BookAlreadyOnLoanException(BookCopyId bookCopyId) {
            super(String.format("Book copy %s is already on loan", bookCopyId));
            this.bookCopyId = bookCopyId;
        }

        public BookCopyId getBookCopyId() {
            return bookCopyId;
        }
    }
}
