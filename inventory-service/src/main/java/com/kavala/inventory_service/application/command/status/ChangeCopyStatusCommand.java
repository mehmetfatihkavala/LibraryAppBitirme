package com.kavala.inventory_service.application.command.status;

import com.kavala.inventory_service.core.cqrs.Command;
import com.kavala.inventory_service.domain.model.CopyStatus;

import java.util.Objects;
import java.util.UUID;

/**
 * Command to change the status of a book copy.
 * Supports various status transitions: AVAILABLE, LOANED, RESERVED, LOST,
 * DAMAGED, WITHDRAWN.
 * 
 * Immutable command object for CQRS pattern.
 */
public class ChangeCopyStatusCommand implements Command<Void> {

    private final UUID bookCopyId;
    private final CopyStatus newStatus;
    private final String reason; // Optional: Reason for status change (e.g., damage description, withdrawal
                                 // reason)

    private ChangeCopyStatusCommand(Builder builder) {
        this.bookCopyId = Objects.requireNonNull(builder.bookCopyId, "BookCopyId cannot be null");
        this.newStatus = Objects.requireNonNull(builder.newStatus, "NewStatus cannot be null");
        this.reason = builder.reason;
    }

    public UUID getBookCopyId() {
        return bookCopyId;
    }

    public CopyStatus getNewStatus() {
        return newStatus;
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
        private CopyStatus newStatus;
        private String reason;

        public Builder bookCopyId(UUID bookCopyId) {
            this.bookCopyId = bookCopyId;
            return this;
        }

        public Builder newStatus(CopyStatus newStatus) {
            this.newStatus = newStatus;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public ChangeCopyStatusCommand build() {
            return new ChangeCopyStatusCommand(this);
        }
    }

    @Override
    public String toString() {
        return String.format("ChangeCopyStatusCommand{bookCopyId=%s, newStatus=%s, reason='%s'}",
                bookCopyId, newStatus, reason);
    }
}
