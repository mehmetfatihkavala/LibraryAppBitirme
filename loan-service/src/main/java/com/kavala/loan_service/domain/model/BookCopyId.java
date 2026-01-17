package com.kavala.loan_service.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing a reference to a BookCopy from inventory-service.
 * Immutable and self-validating.
 */
public final class BookCopyId {

    private final UUID value;

    private BookCopyId(UUID value) {
        this.value = Objects.requireNonNull(value, "BookCopyId value cannot be null");
    }

    public static BookCopyId of(UUID value) {
        return new BookCopyId(value);
    }

    public static BookCopyId of(String value) {
        Objects.requireNonNull(value, "BookCopyId string value cannot be null");
        return new BookCopyId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BookCopyId that = (BookCopyId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
