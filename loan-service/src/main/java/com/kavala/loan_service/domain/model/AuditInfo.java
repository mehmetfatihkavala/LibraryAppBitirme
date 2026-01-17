package com.kavala.loan_service.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object for audit information.
 * Tracks creation and modification timestamps.
 */
public final class AuditInfo {

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private AuditInfo(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
        this.updatedAt = updatedAt;
    }

    /**
     * Creates a new AuditInfo for a newly created entity.
     */
    public static AuditInfo create() {
        LocalDateTime now = LocalDateTime.now();
        return new AuditInfo(now, now);
    }

    /**
     * Creates an AuditInfo with a specific creation time.
     */
    public static AuditInfo createAt(LocalDateTime createdAt) {
        return new AuditInfo(createdAt, createdAt);
    }

    /**
     * Reconstitutes AuditInfo from persistence.
     */
    public static AuditInfo reconstitute(LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new AuditInfo(createdAt, updatedAt);
    }

    /**
     * Creates a new AuditInfo with updated timestamp.
     */
    public AuditInfo markUpdated() {
        return new AuditInfo(this.createdAt, LocalDateTime.now());
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuditInfo auditInfo = (AuditInfo) o;
        return Objects.equals(createdAt, auditInfo.createdAt)
                && Objects.equals(updatedAt, auditInfo.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return String.format("AuditInfo{createdAt=%s, updatedAt=%s}", createdAt, updatedAt);
    }
}
