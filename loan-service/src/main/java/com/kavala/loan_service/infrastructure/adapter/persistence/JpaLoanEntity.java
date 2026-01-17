package com.kavala.loan_service.infrastructure.adapter.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Loan aggregate persistence.
 * Maps domain model to database table.
 */
@Entity
@Table(name = "loans", indexes = {
        @Index(name = "idx_loan_member_id", columnList = "member_id"),
        @Index(name = "idx_loan_book_copy_id", columnList = "book_copy_id"),
        @Index(name = "idx_loan_status", columnList = "status"),
        @Index(name = "idx_loan_due_date", columnList = "due_date")
})
public class JpaLoanEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Column(name = "book_copy_id", nullable = false)
    private UUID bookCopyId;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private LoanStatusEntity status;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "fine_amount", precision = 10, scale = 2)
    private BigDecimal fineAmount;

    @Column(name = "fine_currency", length = 3)
    private String fineCurrency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // JPA requires default constructor
    protected JpaLoanEntity() {
    }

    public JpaLoanEntity(UUID id, UUID memberId, UUID bookCopyId, LoanStatusEntity status,
            LocalDate dueDate, LocalDateTime returnedAt, BigDecimal fineAmount, String fineCurrency,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.bookCopyId = bookCopyId;
        this.status = status;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
        this.fineAmount = fineAmount;
        this.fineCurrency = fineCurrency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public UUID getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(UUID bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public LoanStatusEntity getStatus() {
        return status;
    }

    public void setStatus(LoanStatusEntity status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getFineCurrency() {
        return fineCurrency;
    }

    public void setFineCurrency(String fineCurrency) {
        this.fineCurrency = fineCurrency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Loan status enum for JPA mapping.
     */
    public enum LoanStatusEntity {
        OPEN,
        RETURNED,
        OVERDUE
    }
}
