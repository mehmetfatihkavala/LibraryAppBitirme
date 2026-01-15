package com.kavala.inventory_service.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * BookCopy Aggregate Root.
 * Represents a physical copy of a book in the library inventory.
 * 
 * This aggregate encapsulates:
 * - Identity (BookCopyId)
 * - Reference to catalog (BookId)
 * - Physical identification (Barcode)
 * - Location tracking (ShelfLocation)
 * - Lifecycle status (CopyStatus)
 * - Audit trail (AuditInfo)
 * 
 * Domain events are collected and can be published after persistence.
 */
public class BookCopy {

    private final BookCopyId id;
    private final BookId bookId;
    private final Barcode barcode;
    private ShelfLocation shelfLocation;
    private CopyStatus status;
    private AuditInfo auditInfo;

    // Domain events collected during aggregate operations
    private final List<Object> domainEvents = new ArrayList<>();

    private BookCopy(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "BookCopyId cannot be null");
        this.bookId = Objects.requireNonNull(builder.bookId, "BookId cannot be null");
        this.barcode = Objects.requireNonNull(builder.barcode, "Barcode cannot be null");
        this.shelfLocation = builder.shelfLocation;
        this.status = builder.status != null ? builder.status : CopyStatus.AVAILABLE;
        this.auditInfo = builder.auditInfo != null ? builder.auditInfo : AuditInfo.create();
    }

    // ==================== Factory Methods ====================

    /**
     * Creates a new BookCopy when acquired by the library.
     */
    public static BookCopy acquire(BookId bookId, Barcode barcode, LocalDateTime acquiredAt) {
        BookCopy copy = new Builder()
                .id(BookCopyId.generate())
                .bookId(bookId)
                .barcode(barcode)
                .status(CopyStatus.AVAILABLE)
                .auditInfo(AuditInfo.createWithAcquisition(acquiredAt))
                .build();

        copy.registerEvent(new BookCopyAcquiredEvent(copy.getId(), bookId, barcode, acquiredAt));
        return copy;
    }

    /**
     * Reconstitutes a BookCopy from persistence.
     */
    public static BookCopy reconstitute(
            BookCopyId id,
            BookId bookId,
            Barcode barcode,
            ShelfLocation shelfLocation,
            CopyStatus status,
            AuditInfo auditInfo) {
        return new Builder()
                .id(id)
                .bookId(bookId)
                .barcode(barcode)
                .shelfLocation(shelfLocation)
                .status(status)
                .auditInfo(auditInfo)
                .build();
    }

    // ==================== Domain Behaviors ====================

    /**
     * Marks this copy as loaned out.
     * 
     * @throws IllegalStateException if the copy is not available for loan
     */
    public void markAsLoaned() {
        if (!status.isAvailableForLoan()) {
            throw new IllegalStateException(
                    String.format("Cannot loan copy %s: current status is %s", id, status));
        }
        CopyStatus previousStatus = this.status;
        this.status = status.transitionTo(CopyStatus.LOANED);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyStatusChangedEvent(id, previousStatus, CopyStatus.LOANED));
    }

    /**
     * Marks this copy as returned and available.
     */
    public void markAsReturned() {
        if (status != CopyStatus.LOANED) {
            throw new IllegalStateException(
                    String.format("Cannot return copy %s: current status is %s (expected LOANED)", id, status));
        }
        CopyStatus previousStatus = this.status;
        this.status = status.transitionTo(CopyStatus.AVAILABLE);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyStatusChangedEvent(id, previousStatus, CopyStatus.AVAILABLE));
    }

    /**
     * Reports this copy as lost.
     */
    public void reportLost() {
        CopyStatus previousStatus = this.status;
        this.status = status.transitionTo(CopyStatus.LOST);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyLostEvent(id, bookId));
        registerEvent(new BookCopyStatusChangedEvent(id, previousStatus, CopyStatus.LOST));
    }

    /**
     * Reports this copy as damaged.
     */
    public void reportDamaged(String damageDescription) {
        CopyStatus previousStatus = this.status;
        this.status = status.transitionTo(CopyStatus.DAMAGED);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyDamagedEvent(id, bookId, damageDescription));
        registerEvent(new BookCopyStatusChangedEvent(id, previousStatus, CopyStatus.DAMAGED));
    }

    /**
     * Withdraws this copy from circulation (terminal state).
     */
    public void withdraw(String reason) {
        CopyStatus previousStatus = this.status;
        this.status = status.transitionTo(CopyStatus.WITHDRAWN);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyWithdrawnEvent(id, bookId, reason));
        registerEvent(new BookCopyStatusChangedEvent(id, previousStatus, CopyStatus.WITHDRAWN));
    }

    /**
     * Assigns or updates the shelf location.
     */
    public void assignShelfLocation(ShelfLocation newLocation) {
        Objects.requireNonNull(newLocation, "Shelf location cannot be null");
        ShelfLocation previousLocation = this.shelfLocation;
        this.shelfLocation = newLocation;
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyLocationChangedEvent(id, previousLocation, newLocation));
    }

    /**
     * Removes the shelf location (e.g., for reshelving).
     */
    public void clearShelfLocation() {
        if (this.shelfLocation != null) {
            ShelfLocation previousLocation = this.shelfLocation;
            this.shelfLocation = null;
            this.auditInfo = auditInfo.markUpdated();

            registerEvent(new BookCopyLocationChangedEvent(id, previousLocation, null));
        }
    }

    /**
     * Reserves this copy for a member.
     */
    public void reserve() {
        if (!status.isAvailableForLoan()) {
            throw new IllegalStateException(
                    String.format("Cannot reserve copy %s: current status is %s", id, status));
        }
        CopyStatus previousStatus = this.status;
        this.status = status.transitionTo(CopyStatus.RESERVED);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyStatusChangedEvent(id, previousStatus, CopyStatus.RESERVED));
    }

    /**
     * Cancels reservation and makes copy available again.
     */
    public void cancelReservation() {
        if (status != CopyStatus.RESERVED) {
            throw new IllegalStateException(
                    String.format("Cannot cancel reservation for copy %s: current status is %s", id, status));
        }
        CopyStatus previousStatus = this.status;
        this.status = status.transitionTo(CopyStatus.AVAILABLE);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new BookCopyStatusChangedEvent(id, previousStatus, CopyStatus.AVAILABLE));
    }

    // ==================== Event Management ====================

    private void registerEvent(Object event) {
        domainEvents.add(event);
    }

    /**
     * Returns and clears all pending domain events.
     */
    public List<Object> pullDomainEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return Collections.unmodifiableList(events);
    }

    /**
     * Returns pending domain events without clearing them.
     */
    public List<Object> peekDomainEvents() {
        return Collections.unmodifiableList(new ArrayList<>(domainEvents));
    }

    // ==================== Getters ====================

    public BookCopyId getId() {
        return id;
    }

    public BookId getBookId() {
        return bookId;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public Optional<ShelfLocation> getShelfLocation() {
        return Optional.ofNullable(shelfLocation);
    }

    public CopyStatus getStatus() {
        return status;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public boolean isAvailable() {
        return status.isAvailableForLoan();
    }

    // ==================== Builder ====================

    public static class Builder {
        private BookCopyId id;
        private BookId bookId;
        private Barcode barcode;
        private ShelfLocation shelfLocation;
        private CopyStatus status;
        private AuditInfo auditInfo;

        public Builder id(BookCopyId id) {
            this.id = id;
            return this;
        }

        public Builder bookId(BookId bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder barcode(Barcode barcode) {
            this.barcode = barcode;
            return this;
        }

        public Builder shelfLocation(ShelfLocation shelfLocation) {
            this.shelfLocation = shelfLocation;
            return this;
        }

        public Builder status(CopyStatus status) {
            this.status = status;
            return this;
        }

        public Builder auditInfo(AuditInfo auditInfo) {
            this.auditInfo = auditInfo;
            return this;
        }

        public BookCopy build() {
            return new BookCopy(this);
        }
    }

    // ==================== Domain Events ====================

    public record BookCopyAcquiredEvent(
            BookCopyId copyId,
            BookId bookId,
            Barcode barcode,
            LocalDateTime acquiredAt) {
    }

    public record BookCopyStatusChangedEvent(
            BookCopyId copyId,
            CopyStatus previousStatus,
            CopyStatus newStatus) {
    }

    public record BookCopyLocationChangedEvent(
            BookCopyId copyId,
            ShelfLocation previousLocation,
            ShelfLocation newLocation) {
    }

    public record BookCopyLostEvent(
            BookCopyId copyId,
            BookId bookId) {
    }

    public record BookCopyDamagedEvent(
            BookCopyId copyId,
            BookId bookId,
            String damageDescription) {
    }

    public record BookCopyWithdrawnEvent(
            BookCopyId copyId,
            BookId bookId,
            String reason) {
    }

    // ==================== Object Methods ====================

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BookCopy bookCopy = (BookCopy) o;
        return Objects.equals(id, bookCopy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("BookCopy{id=%s, bookId=%s, barcode=%s, status=%s, location=%s}",
                id, bookId, barcode, status, shelfLocation);
    }
}
