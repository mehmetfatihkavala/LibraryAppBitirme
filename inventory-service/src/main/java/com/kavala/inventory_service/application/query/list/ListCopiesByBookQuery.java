package com.kavala.inventory_service.application.query.list;

import com.kavala.inventory_service.core.cqrs.Query;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Query to list all copies of a specific book.
 * Part of CQRS query pattern for inventory management.
 */
public class ListCopiesByBookQuery implements Query<ListCopiesByBookQuery.Result> {

    private final UUID bookId;
    private final Boolean availableOnly; // Optional filter

    private ListCopiesByBookQuery(Builder builder) {
        this.bookId = Objects.requireNonNull(builder.bookId, "BookId cannot be null");
        this.availableOnly = builder.availableOnly;
    }

    public UUID getBookId() {
        return bookId;
    }

    public Boolean getAvailableOnly() {
        return availableOnly;
    }

    public boolean isAvailableOnlyFilter() {
        return Boolean.TRUE.equals(availableOnly);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ListCopiesByBookQuery of(UUID bookId) {
        return builder().bookId(bookId).build();
    }

    public static class Builder {
        private UUID bookId;
        private Boolean availableOnly;

        public Builder bookId(UUID bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder availableOnly(Boolean availableOnly) {
            this.availableOnly = availableOnly;
            return this;
        }

        public ListCopiesByBookQuery build() {
            return new ListCopiesByBookQuery(this);
        }
    }

    /**
     * Single copy item in the result list.
     */
    public record CopyItem(
            UUID id,
            String barcode,
            String status,
            String statusDescription,
            String shelfLocation,
            boolean isAvailable) {
    }

    /**
     * Query result containing list of copies.
     */
    public record Result(
            UUID bookId,
            List<CopyItem> copies,
            int totalCount,
            int availableCount) {
    }

    @Override
    public String toString() {
        return String.format("ListCopiesByBookQuery{bookId=%s, availableOnly=%s}", bookId, availableOnly);
    }
}
