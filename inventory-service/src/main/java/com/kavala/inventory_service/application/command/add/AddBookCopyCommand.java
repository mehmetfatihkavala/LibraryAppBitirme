package com.kavala.inventory_service.application.command.add;

import com.kavala.inventory_service.core.cqrs.Command;
import com.kavala.inventory_service.domain.model.BookCopyId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Command to add a new book copy to the inventory.
 * Part of CQRS command pattern for inventory management.
 * 
 * Immutable command object containing all necessary data
 * to create a new BookCopy aggregate.
 */
public class AddBookCopyCommand implements Command<BookCopyId> {

    private final UUID bookId;
    private final String barcode;
    private final String shelfLocation; // Optional: Format "FLOOR-SECTION-SHELF[-POSITION]"
    private final LocalDateTime acquiredAt;

    private AddBookCopyCommand(Builder builder) {
        this.bookId = Objects.requireNonNull(builder.bookId, "BookId cannot be null");
        this.barcode = Objects.requireNonNull(builder.barcode, "Barcode cannot be null");
        this.shelfLocation = builder.shelfLocation;
        this.acquiredAt = builder.acquiredAt != null ? builder.acquiredAt : LocalDateTime.now();
    }

    public UUID getBookId() {
        return bookId;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public LocalDateTime getAcquiredAt() {
        return acquiredAt;
    }

    public boolean hasShelfLocation() {
        return shelfLocation != null && !shelfLocation.isBlank();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID bookId;
        private String barcode;
        private String shelfLocation;
        private LocalDateTime acquiredAt;

        public Builder bookId(UUID bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public Builder shelfLocation(String shelfLocation) {
            this.shelfLocation = shelfLocation;
            return this;
        }

        public Builder acquiredAt(LocalDateTime acquiredAt) {
            this.acquiredAt = acquiredAt;
            return this;
        }

        public AddBookCopyCommand build() {
            return new AddBookCopyCommand(this);
        }
    }

    @Override
    public String toString() {
        return String.format("AddBookCopyCommand{bookId=%s, barcode='%s', shelfLocation='%s', acquiredAt=%s}",
                bookId, barcode, shelfLocation, acquiredAt);
    }
}
