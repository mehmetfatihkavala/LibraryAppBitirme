package com.kavala.inventory_service.application.command.remove;

import com.kavala.inventory_service.core.cqrs.Command;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to remove a book copy from the inventory.
 * Part of CQRS command pattern for inventory management.
 * 
 * Note: This is a hard delete. For soft delete (marking as withdrawn),
 * use ChangeCopyStatusCommand with WITHDRAWN status.
 */
public class RemoveBookCopyCommand implements Command<Void> {

    private final UUID bookCopyId;
    private final String reason; // Optional: Reason for removal

    private RemoveBookCopyCommand(Builder builder) {
        this.bookCopyId = Objects.requireNonNull(builder.bookCopyId, "BookCopyId cannot be null");
        this.reason = builder.reason;
    }

    public UUID getBookCopyId() {
        return bookCopyId;
    }

    public String getReason() {
        return reason;
    }

    public boolean hasReason() {
        return reason != null && !reason.isBlank();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID bookCopyId;
        private String reason;

        public Builder bookCopyId(UUID bookCopyId) {
            this.bookCopyId = bookCopyId;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RemoveBookCopyCommand build() {
            return new RemoveBookCopyCommand(this);
        }
    }

    @Override
    public String toString() {
        return String.format("RemoveBookCopyCommand{bookCopyId=%s, reason='%s'}",
                bookCopyId, reason);
    }
}
