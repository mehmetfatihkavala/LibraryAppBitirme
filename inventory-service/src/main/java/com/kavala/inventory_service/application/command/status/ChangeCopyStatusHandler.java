package com.kavala.inventory_service.application.command.status;

import com.kavala.inventory_service.core.cqrs.CommandHandler;
import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookCopyId;
import com.kavala.inventory_service.domain.model.CopyStatus;
import com.kavala.inventory_service.domain.port.BookCopyRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Handler for ChangeCopyStatusCommand.
 * Implements status transitions for book copies according to the domain state
 * machine.
 * 
 * Status transitions are validated by the BookCopy aggregate to ensure
 * domain invariants are maintained.
 */
@Service
@Transactional
public class ChangeCopyStatusHandler implements CommandHandler<ChangeCopyStatusCommand, Void> {

    private final BookCopyRepository bookCopyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ChangeCopyStatusHandler(
            BookCopyRepository bookCopyRepository,
            ApplicationEventPublisher eventPublisher) {
        this.bookCopyRepository = Objects.requireNonNull(bookCopyRepository, "BookCopyRepository cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "EventPublisher cannot be null");
    }

    @Override
    public Void handle(ChangeCopyStatusCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        BookCopyId copyId = BookCopyId.of(command.getBookCopyId());

        // Find the book copy
        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new BookCopyNotFoundException(copyId));

        // Apply the appropriate status transition based on target status
        applyStatusTransition(bookCopy, command.getNewStatus(), command.getReason());

        // Persist changes
        bookCopyRepository.save(bookCopy);

        // Publish domain events
        bookCopy.pullDomainEvents().forEach(eventPublisher::publishEvent);

        return null;
    }

    private void applyStatusTransition(BookCopy bookCopy, CopyStatus targetStatus, String reason) {
        switch (targetStatus) {
            case AVAILABLE -> handleTransitionToAvailable(bookCopy);
            case LOANED -> bookCopy.markAsLoaned();
            case RESERVED -> bookCopy.reserve();
            case LOST -> bookCopy.reportLost();
            case DAMAGED -> bookCopy.reportDamaged(reason != null ? reason : "No description provided");
            case WITHDRAWN -> bookCopy.withdraw(reason != null ? reason : "No reason provided");
        }
    }

    private void handleTransitionToAvailable(BookCopy bookCopy) {
        CopyStatus currentStatus = bookCopy.getStatus();
        switch (currentStatus) {
            case LOANED -> bookCopy.markAsReturned();
            case RESERVED -> bookCopy.cancelReservation();
            default -> throw new IllegalStateException(
                    String.format("Cannot transition from %s to AVAILABLE directly", currentStatus));
        }
    }

    /**
     * Exception thrown when a book copy is not found.
     */
    public static class BookCopyNotFoundException extends RuntimeException {
        private final BookCopyId bookCopyId;

        public BookCopyNotFoundException(BookCopyId bookCopyId) {
            super(String.format("Book copy with ID %s not found", bookCopyId.getValue()));
            this.bookCopyId = bookCopyId;
        }

        public BookCopyId getBookCopyId() {
            return bookCopyId;
        }
    }
}
