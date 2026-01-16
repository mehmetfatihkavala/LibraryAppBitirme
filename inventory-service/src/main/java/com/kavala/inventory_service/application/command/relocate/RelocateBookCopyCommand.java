package com.kavala.inventory_service.application.command.relocate;

import com.kavala.inventory_service.core.cqrs.Command;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to relocate a book copy to a new shelf location.
 * Part of CQRS command pattern for inventory management.
 * 
 * Immutable command object for changing the physical location
 * of a book copy in the library.
 */
public class RelocateBookCopyCommand implements Command<Void> {

    private final UUID bookCopyId;
    private final String newShelfLocation; // Format: "FLOOR-SECTION-SHELF[-POSITION]"

    private RelocateBookCopyCommand(Builder builder) {
        this.bookCopyId = Objects.requireNonNull(builder.bookCopyId, "BookCopyId cannot be null");
        this.newShelfLocation = builder.newShelfLocation; // Can be null to clear location
    }

    public UUID getBookCopyId() {
        return bookCopyId;
    }

    public String getNewShelfLocation() {
        return newShelfLocation;
    }

    public boolean hasClearLocationRequest() {
        return newShelfLocation == null || newShelfLocation.isBlank();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID bookCopyId;
        private String newShelfLocation;

        public Builder bookCopyId(UUID bookCopyId) {
            this.bookCopyId = bookCopyId;
            return this;
        }

        public Builder newShelfLocation(String newShelfLocation) {
            this.newShelfLocation = newShelfLocation;
            return this;
        }

        /**
         * Sets location to null, indicating the copy should have its location cleared.
         */
        public Builder clearLocation() {
            this.newShelfLocation = null;
            return this;
        }

        public RelocateBookCopyCommand build() {
            return new RelocateBookCopyCommand(this);
        }
    }

    @Override
    public String toString() {
        return String.format("RelocateBookCopyCommand{bookCopyId=%s, newShelfLocation='%s'}",
                bookCopyId, newShelfLocation);
    }
}
