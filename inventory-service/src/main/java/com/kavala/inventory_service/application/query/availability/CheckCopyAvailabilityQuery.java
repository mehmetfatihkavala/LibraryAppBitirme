package com.kavala.inventory_service.application.query.availability;

import com.kavala.inventory_service.core.cqrs.Query;

import java.util.Objects;
import java.util.UUID;

/**
 * Query to check availability of copies for a specific book.
 * Part of CQRS query pattern for inventory management.
 */
public class CheckCopyAvailabilityQuery implements Query<CheckCopyAvailabilityQuery.Result> {

    private final UUID bookId;

    private CheckCopyAvailabilityQuery(UUID bookId) {
        this.bookId = Objects.requireNonNull(bookId, "BookId cannot be null");
    }

    public static CheckCopyAvailabilityQuery of(UUID bookId) {
        return new CheckCopyAvailabilityQuery(bookId);
    }

    public UUID getBookId() {
        return bookId;
    }

    /**
     * Query result containing availability information.
     */
    public record Result(
            UUID bookId,
            boolean hasAvailableCopy,
            long totalCopies,
            long availableCopies,
            long loanedCopies,
            long reservedCopies) {
        public boolean isFullyAvailable() {
            return totalCopies > 0 && availableCopies == totalCopies;
        }

        public boolean isCompletelyUnavailable() {
            return totalCopies > 0 && availableCopies == 0;
        }

        public double getAvailabilityPercentage() {
            return totalCopies > 0 ? (double) availableCopies / totalCopies * 100.0 : 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("CheckCopyAvailabilityQuery{bookId=%s}", bookId);
    }
}
