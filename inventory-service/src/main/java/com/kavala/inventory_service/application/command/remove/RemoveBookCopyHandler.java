package com.kavala.inventory_service.application.command.remove;

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
 * Handler for RemoveBookCopyCommand.
 * Implements the use case of removing a book copy from the inventory.
 * 
 * Business rules:
 * - Cannot remove a copy that is currently loaned
 * - Cannot remove a reserved copy
 * - Publishes BookCopyRemovedEvent for integration with other services
 */
@Service
@Transactional
public class RemoveBookCopyHandler implements CommandHandler<RemoveBookCopyCommand, Void> {

    private final BookCopyRepository bookCopyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RemoveBookCopyHandler(
            BookCopyRepository bookCopyRepository,
            ApplicationEventPublisher eventPublisher) {
        this.bookCopyRepository = Objects.requireNonNull(bookCopyRepository, "BookCopyRepository cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "EventPublisher cannot be null");
    }

    @Override
    public Void handle(RemoveBookCopyCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        BookCopyId copyId = BookCopyId.of(command.getBookCopyId());

        // Find the book copy
        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new BookCopyNotFoundException(copyId));

        // Validate business rules
        validateCanBeRemoved(bookCopy);

        // Delete from repository
        bookCopyRepository.delete(bookCopy);

        // Publish removal event
        BookCopyRemovedEvent event = new BookCopyRemovedEvent(
                copyId,
                bookCopy.getBookId(),
                bookCopy.getBarcode(),
                command.getReason());
        eventPublisher.publishEvent(event);

        return null;
    }

    private void validateCanBeRemoved(BookCopy bookCopy) {
        CopyStatus status = bookCopy.getStatus();

        if (status == CopyStatus.LOANED) {
            throw new CannotRemoveLoanedCopyException(bookCopy.getId());
        }

        if (status == CopyStatus.RESERVED) {
            throw new CannotRemoveReservedCopyException(bookCopy.getId());
        }
    }

    /**
     * Event published when a book copy is removed from inventory.
     */
    public record BookCopyRemovedEvent(
            BookCopyId copyId,
            com.kavala.inventory_service.domain.model.BookId bookId,
            com.kavala.inventory_service.domain.model.Barcode barcode,
            String reason) {
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

    /**
     * Exception thrown when trying to remove a loaned copy.
     */
    public static class CannotRemoveLoanedCopyException extends RuntimeException {
        private final BookCopyId bookCopyId;

        public CannotRemoveLoanedCopyException(BookCopyId bookCopyId) {
            super(String.format("Cannot remove book copy %s: it is currently loaned", bookCopyId.getValue()));
            this.bookCopyId = bookCopyId;
        }

        public BookCopyId getBookCopyId() {
            return bookCopyId;
        }
    }

    /**
     * Exception thrown when trying to remove a reserved copy.
     */
    public static class CannotRemoveReservedCopyException extends RuntimeException {
        private final BookCopyId bookCopyId;

        public CannotRemoveReservedCopyException(BookCopyId bookCopyId) {
            super(String.format("Cannot remove book copy %s: it is currently reserved", bookCopyId.getValue()));
            this.bookCopyId = bookCopyId;
        }

        public BookCopyId getBookCopyId() {
            return bookCopyId;
        }
    }
}
