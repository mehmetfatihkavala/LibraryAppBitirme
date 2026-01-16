package com.kavala.inventory_service.application.command.relocate;

import com.kavala.inventory_service.core.cqrs.CommandHandler;
import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookCopyId;
import com.kavala.inventory_service.domain.model.ShelfLocation;
import com.kavala.inventory_service.domain.port.BookCopyRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Handler for RelocateBookCopyCommand.
 * Implements the use case of relocating a book copy to a new shelf location.
 * 
 * Supports both assigning a new location and clearing the current location.
 * Publishes BookCopyLocationChangedEvent for integration with other services.
 */
@Service
@Transactional
public class RelocateBookCopyHandler implements CommandHandler<RelocateBookCopyCommand, Void> {

    private final BookCopyRepository bookCopyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RelocateBookCopyHandler(
            BookCopyRepository bookCopyRepository,
            ApplicationEventPublisher eventPublisher) {
        this.bookCopyRepository = Objects.requireNonNull(bookCopyRepository, "BookCopyRepository cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "EventPublisher cannot be null");
    }

    @Override
    public Void handle(RelocateBookCopyCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        BookCopyId copyId = BookCopyId.of(command.getBookCopyId());

        // Find the book copy
        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new BookCopyNotFoundException(copyId));

        // Apply location change or clear
        if (command.hasClearLocationRequest()) {
            bookCopy.clearShelfLocation();
        } else {
            ShelfLocation newLocation = ShelfLocation.parse(command.getNewShelfLocation());
            bookCopy.assignShelfLocation(newLocation);
        }

        // Persist changes
        bookCopyRepository.save(bookCopy);

        // Publish domain events
        bookCopy.pullDomainEvents().forEach(eventPublisher::publishEvent);

        return null;
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
