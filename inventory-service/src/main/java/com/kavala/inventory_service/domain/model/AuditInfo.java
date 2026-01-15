package com.kavala.inventory_service.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Value Object for audit information (createdAt, updatedAt, acquiredAt).
 * Tracks when a BookCopy was created, last updated, and acquired.
 * Immutable with factory methods for updates.
 */
public final class AuditInfo {

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime acquiredAt;

    private AuditInfo(LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime acquiredAt) {
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
        this.updatedAt = updatedAt;
        this.acquiredAt = acquiredAt;
    }

    /**
     * Creates a new AuditInfo for a newly created entity.
     */
    public static AuditInfo create() {
        LocalDateTime now = LocalDateTime.now();
        return new AuditInfo(now, now, null);
    }

    /**
     * Creates a new AuditInfo with acquisition date.
     */
    public static AuditInfo createWithAcquisition(LocalDateTime acquiredAt) {
        LocalDateTime now = LocalDateTime.now();
        return new AuditInfo(now, now, acquiredAt);
    }

    /**
     * Creates an AuditInfo from existing values (e.g., from database).
     */
    public static AuditInfo of(LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime acquiredAt) {
        return new AuditInfo(createdAt, updatedAt, acquiredAt);
    }

    /**
     * Returns a new AuditInfo with updated timestamp.
     */
    public AuditInfo markUpdated() {
        return new AuditInfo(this.createdAt, LocalDateTime.now(), this.acquiredAt);
    }

    /**
     * Returns a new AuditInfo with acquisition date set.
     */
    public AuditInfo withAcquisitionDate(LocalDateTime acquiredAt) {
        return new AuditInfo(this.createdAt, LocalDateTime.now(), acquiredAt);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateTime> getUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public Optional<LocalDateTime> getAcquiredAt() {
        return Optional.ofNullable(acquiredAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuditInfo auditInfo = (AuditInfo) o;
        return Objects.equals(createdAt, auditInfo.createdAt) &&
                Objects.equals(updatedAt, auditInfo.updatedAt) &&
                Objects.equals(acquiredAt, auditInfo.acquiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, acquiredAt);
    }

    @Override
    public String toString() {
        return String.format("AuditInfo{createdAt=%s, updatedAt=%s, acquiredAt=%s}",
                createdAt, updatedAt, acquiredAt);
    }
}
