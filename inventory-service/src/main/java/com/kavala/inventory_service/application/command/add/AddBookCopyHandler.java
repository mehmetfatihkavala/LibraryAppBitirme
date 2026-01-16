package com.kavala.inventory_service.application.command.add;

import com.kavala.inventory_service.core.cqrs.CommandHandler;
import com.kavala.inventory_service.domain.model.Barcode;
import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookCopyId;
import com.kavala.inventory_service.domain.model.BookId;
import com.kavala.inventory_service.domain.model.ShelfLocation;
import com.kavala.inventory_service.domain.port.BookCopyRepository;
import com.kavala.inventory_service.domain.port.CatalogBookLookupPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Handler for AddBookCopyCommand.
 * Implements the use case of adding a new physical book copy to the inventory.
 * 
 * Follows CQRS pattern by separating command handling from queries.
 * Uses domain events for eventual consistency with other bounded contexts.
 */
@Service
@Transactional
public class AddBookCopyHandler implements CommandHandler<AddBookCopyCommand, BookCopyId> {

    private final BookCopyRepository bookCopyRepository;
    private final CatalogBookLookupPort catalogBookLookupPort;
    private final ApplicationEventPublisher eventPublisher;

    public AddBookCopyHandler(
            BookCopyRepository bookCopyRepository,
            CatalogBookLookupPort catalogBookLookupPort,
            ApplicationEventPublisher eventPublisher) {
        this.bookCopyRepository = Objects.requireNonNull(bookCopyRepository, "BookCopyRepository cannot be null");
        this.catalogBookLookupPort = Objects.requireNonNull(catalogBookLookupPort,
                "CatalogBookLookupPort cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "EventPublisher cannot be null");
    }

    @Override
    public BookCopyId handle(AddBookCopyCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        // Create value objects from command
        BookId bookId = BookId.of(command.getBookId());
        Barcode barcode = Barcode.of(command.getBarcode());

        // Validate: Book must exist in catalog
        catalogBookLookupPort.validateBookExists(bookId);

        // Validate: Barcode must be unique
        if (bookCopyRepository.existsByBarcode(barcode)) {
            throw new DuplicateBarcodeException(barcode);
        }

        // Create the aggregate using factory method
        BookCopy bookCopy = BookCopy.acquire(bookId, barcode, command.getAcquiredAt());

        // Assign shelf location if provided
        if (command.hasShelfLocation()) {
            ShelfLocation location = ShelfLocation.parse(command.getShelfLocation());
            bookCopy.assignShelfLocation(location);
        }

        // Persist the aggregate
        BookCopy savedCopy = bookCopyRepository.save(bookCopy);

        // Publish domain events
        savedCopy.pullDomainEvents().forEach(eventPublisher::publishEvent);

        return savedCopy.getId();
    }

    /**
     * Exception thrown when a barcode already exists in the system.
     */
    public static class DuplicateBarcodeException extends RuntimeException {
        private final Barcode barcode;

        public DuplicateBarcodeException(Barcode barcode) {
            super(String.format("Book copy with barcode '%s' already exists", barcode.getValue()));
            this.barcode = barcode;
        }

        public Barcode getBarcode() {
            return barcode;
        }
    }
}
