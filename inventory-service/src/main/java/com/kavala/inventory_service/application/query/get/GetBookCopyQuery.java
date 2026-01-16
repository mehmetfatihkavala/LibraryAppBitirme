package com.kavala.inventory_service.application.query.get;

import com.kavala.inventory_service.core.cqrs.Query;

import java.util.Objects;
import java.util.UUID;

/**
 * Query to retrieve a single book copy by its ID.
 * Part of CQRS query pattern for inventory management.
 */
public class GetBookCopyQuery implements Query<GetBookCopyQuery.Result> {

    private final UUID bookCopyId;

    private GetBookCopyQuery(UUID bookCopyId) {
        this.bookCopyId = Objects.requireNonNull(bookCopyId, "BookCopyId cannot be null");
    }

    public static GetBookCopyQuery of(UUID bookCopyId) {
        return new GetBookCopyQuery(bookCopyId);
    }

    public UUID getBookCopyId() {
        return bookCopyId;
    }

    /**
     * Query result containing book copy details.
     */
    public record Result(
            UUID id,
            UUID bookId,
            String barcode,
            String status,
            String statusDescription,
            String shelfLocation,
            boolean isAvailable,
            java.time.LocalDateTime createdAt,
            java.time.LocalDateTime updatedAt) {
    }

    @Override
    public String toString() {
        return String.format("GetBookCopyQuery{bookCopyId=%s}", bookCopyId);
    }
}
