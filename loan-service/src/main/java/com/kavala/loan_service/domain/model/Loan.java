package com.kavala.loan_service.domain.model;

import com.kavala.loan_service.domain.event.DomainEvent;
import com.kavala.loan_service.domain.event.FineCalculated;
import com.kavala.loan_service.domain.event.LoanOpened;
import com.kavala.loan_service.domain.event.LoanOverdue;
import com.kavala.loan_service.domain.event.LoanReturned;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Loan Aggregate Root.
 * Represents a book loan transaction in the library system.
 * 
 * This aggregate encapsulates:
 * - Identity (LoanId)
 * - Member reference (MemberId)
 * - Book copy reference (BookCopyId)
 * - Loan lifecycle status (LoanStatus)
 * - Due date tracking (DueDate)
 * - Fine calculation (FineAmount)
 * - Audit trail (AuditInfo)
 * 
 * Domain events are collected and can be published after persistence.
 */
public class Loan {

    private final LoanId id;
    private final MemberId memberId;
    private final BookCopyId bookCopyId;
    private LoanStatus status;
    private final DueDate dueDate;
    private LocalDateTime returnedAt;
    private FineAmount fineAmount;
    private AuditInfo auditInfo;

    // Domain events collected during aggregate operations
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Loan(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "LoanId cannot be null");
        this.memberId = Objects.requireNonNull(builder.memberId, "MemberId cannot be null");
        this.bookCopyId = Objects.requireNonNull(builder.bookCopyId, "BookCopyId cannot be null");
        this.dueDate = Objects.requireNonNull(builder.dueDate, "DueDate cannot be null");
        this.status = builder.status != null ? builder.status : LoanStatus.OPEN;
        this.returnedAt = builder.returnedAt;
        this.fineAmount = builder.fineAmount;
        this.auditInfo = builder.auditInfo != null ? builder.auditInfo : AuditInfo.create();
    }

    // ==================== Factory Methods ====================

    /**
     * Creates a new Loan (checkout operation).
     */
    public static Loan checkout(MemberId memberId, BookCopyId bookCopyId, int loanDays) {
        Loan loan = new Builder()
                .id(LoanId.generate())
                .memberId(memberId)
                .bookCopyId(bookCopyId)
                .dueDate(DueDate.fromDaysFromNow(loanDays))
                .status(LoanStatus.OPEN)
                .auditInfo(AuditInfo.create())
                .build();

        loan.registerEvent(new LoanOpened(
                loan.getId(),
                memberId,
                bookCopyId,
                loan.getDueDate(),
                LocalDateTime.now()));

        return loan;
    }

    /**
     * Reconstitutes a Loan from persistence.
     */
    public static Loan reconstitute(
            LoanId id,
            MemberId memberId,
            BookCopyId bookCopyId,
            LoanStatus status,
            DueDate dueDate,
            LocalDateTime returnedAt,
            FineAmount fineAmount,
            AuditInfo auditInfo) {
        return new Builder()
                .id(id)
                .memberId(memberId)
                .bookCopyId(bookCopyId)
                .status(status)
                .dueDate(dueDate)
                .returnedAt(returnedAt)
                .fineAmount(fineAmount)
                .auditInfo(auditInfo)
                .build();
    }

    // ==================== Domain Behaviors ====================

    /**
     * Marks the loan as returned.
     *
     * @throws IllegalStateException if the loan is already returned
     */
    public void returnBook() {
        if (status == LoanStatus.RETURNED) {
            throw new IllegalStateException(
                    String.format("Loan %s is already returned", id));
        }

        LoanStatus previousStatus = this.status;
        this.status = LoanStatus.RETURNED;
        this.returnedAt = LocalDateTime.now();
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new LoanReturned(
                id,
                memberId,
                bookCopyId,
                previousStatus,
                returnedAt,
                fineAmount));
    }

    /**
     * Marks the loan as overdue.
     *
     * @throws IllegalStateException if the loan is already returned or overdue
     */
    public void markOverdue() {
        if (status == LoanStatus.RETURNED) {
            throw new IllegalStateException(
                    String.format("Cannot mark returned loan %s as overdue", id));
        }
        if (status == LoanStatus.OVERDUE) {
            throw new IllegalStateException(
                    String.format("Loan %s is already marked as overdue", id));
        }
        if (!dueDate.isOverdue()) {
            throw new IllegalStateException(
                    String.format("Loan %s is not yet overdue (due: %s)", id, dueDate));
        }

        LoanStatus previousStatus = this.status;
        this.status = status.transitionTo(LoanStatus.OVERDUE);
        this.auditInfo = auditInfo.markUpdated();

        registerEvent(new LoanOverdue(
                id,
                memberId,
                bookCopyId,
                dueDate,
                dueDate.daysOverdue(),
                LocalDateTime.now()));
    }

    /**
     * Calculates and sets the fine amount based on days overdue.
     *
     * @return the calculated fine amount
     */
    public FineAmount calculateFine() {
        long daysOverdue = dueDate.daysOverdue();
        this.fineAmount = FineAmount.calculateFromDaysOverdue(daysOverdue);
        this.auditInfo = auditInfo.markUpdated();

        if (!fineAmount.isZero()) {
            registerEvent(new FineCalculated(
                    id,
                    memberId,
                    fineAmount,
                    daysOverdue,
                    LocalDateTime.now()));
        }

        return fineAmount;
    }

    /**
     * Checks if this loan is currently overdue.
     */
    public boolean isOverdue() {
        return status == LoanStatus.OVERDUE ||
                (status == LoanStatus.OPEN && dueDate.isOverdue());
    }

    /**
     * Checks if this loan is still active (not returned).
     */
    public boolean isActive() {
        return status.isActive();
    }

    // ==================== Event Management ====================

    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    /**
     * Returns and clears all pending domain events.
     */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return Collections.unmodifiableList(events);
    }

    /**
     * Returns pending domain events without clearing them.
     */
    public List<DomainEvent> peekDomainEvents() {
        return Collections.unmodifiableList(new ArrayList<>(domainEvents));
    }

    // ==================== Getters ====================

    public LoanId getId() {
        return id;
    }

    public MemberId getMemberId() {
        return memberId;
    }

    public BookCopyId getBookCopyId() {
        return bookCopyId;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public DueDate getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public FineAmount getFineAmount() {
        return fineAmount;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    // ==================== Builder ====================

    public static class Builder {
        private LoanId id;
        private MemberId memberId;
        private BookCopyId bookCopyId;
        private LoanStatus status;
        private DueDate dueDate;
        private LocalDateTime returnedAt;
        private FineAmount fineAmount;
        private AuditInfo auditInfo;

        public Builder id(LoanId id) {
            this.id = id;
            return this;
        }

        public Builder memberId(MemberId memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder bookCopyId(BookCopyId bookCopyId) {
            this.bookCopyId = bookCopyId;
            return this;
        }

        public Builder status(LoanStatus status) {
            this.status = status;
            return this;
        }

        public Builder dueDate(DueDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder returnedAt(LocalDateTime returnedAt) {
            this.returnedAt = returnedAt;
            return this;
        }

        public Builder fineAmount(FineAmount fineAmount) {
            this.fineAmount = fineAmount;
            return this;
        }

        public Builder auditInfo(AuditInfo auditInfo) {
            this.auditInfo = auditInfo;
            return this;
        }

        public Loan build() {
            return new Loan(this);
        }
    }

    // ==================== Object Methods ====================

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Loan{id=%s, memberId=%s, bookCopyId=%s, status=%s, dueDate=%s}",
                id, memberId, bookCopyId, status, dueDate);
    }
}
