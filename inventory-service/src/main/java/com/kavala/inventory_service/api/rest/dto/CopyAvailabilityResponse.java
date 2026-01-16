package com.kavala.inventory_service.api.rest.dto;

import java.util.UUID;

/**
 * Response DTO for copy availability check.
 */
public record CopyAvailabilityResponse(
        UUID bookId,
        boolean hasAvailableCopy,
        long totalCopies,
        long availableCopies,
        long loanedCopies,
        long reservedCopies,
        double availabilityPercentage) {
    public boolean isFullyAvailable() {
        return totalCopies > 0 && availableCopies == totalCopies;
    }

    public boolean isCompletelyUnavailable() {
        return totalCopies > 0 && availableCopies == 0;
    }
}
