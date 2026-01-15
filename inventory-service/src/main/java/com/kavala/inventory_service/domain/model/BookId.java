package com.kavala.inventory_service.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing the reference to a Book in the Catalog service.
 * This is an external reference (Anti-Corruption Layer pattern).
 * Immutable and self-validating.
 */
public final class BookId {

    private final UUID value;

    private BookId(UUID value) {
        this.value = Objects.requireNonNull(value, "BookId value cannot be null");
    }

    public static BookId of(UUID value) {
        return new BookId(value);
    }

    public static BookId of(String value) {
        Objects.requireNonNull(value, "BookId string value cannot be null");
        return new BookId(UUID.fromString(value));
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
        BookId bookId = (BookId) o;
        return Objects.equals(value, bookId.value);
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
