package com.kavala.inventory_service.infrastructure.adapter.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for BookCopy aggregate persistence.
 * Maps domain model to database table.
 */
@Entity
@Table(name = "book_copies", indexes = {
        @Index(name = "idx_book_copy_barcode", columnList = "barcode", unique = true),
        @Index(name = "idx_book_copy_book_id", columnList = "book_id"),
        @Index(name = "idx_book_copy_status", columnList = "status"),
        @Index(name = "idx_book_copy_section", columnList = "section")
})
public class JpaBookCopyEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "book_id", nullable = false)
    private UUID bookId;

    @Column(name = "barcode", nullable = false, unique = true, length = 50)
    private String barcode;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CopyStatusEntity status;

    @Column(name = "floor", length = 10)
    private String floor;

    @Column(name = "section", length = 10)
    private String section;

    @Column(name = "shelf", length = 10)
    private String shelf;

    @Column(name = "position", length = 10)
    private String position;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "acquired_at")
    private LocalDateTime acquiredAt;

    // JPA requires default constructor
    protected JpaBookCopyEntity() {
    }

    public JpaBookCopyEntity(UUID id, UUID bookId, String barcode, CopyStatusEntity status,
            String floor, String section, String shelf, String position,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime acquiredAt) {
        this.id = id;
        this.bookId = bookId;
        this.barcode = barcode;
        this.status = status;
        this.floor = floor;
        this.section = section;
        this.shelf = shelf;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.acquiredAt = acquiredAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public CopyStatusEntity getStatus() {
        return status;
    }

    public void setStatus(CopyStatusEntity status) {
        this.status = status;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public LocalDateTime getAcquiredAt() {
        return acquiredAt;
    }

    public void setAcquiredAt(LocalDateTime acquiredAt) {
        this.acquiredAt = acquiredAt;
    }

    public boolean hasShelfLocation() {
        return floor != null && section != null && shelf != null;
    }

    /**
     * Copy status enum for JPA mapping.
     */
    public enum CopyStatusEntity {
        AVAILABLE,
        LOANED,
        RESERVED,
        LOST,
        DAMAGED,
        WITHDRAWN
    }
}
