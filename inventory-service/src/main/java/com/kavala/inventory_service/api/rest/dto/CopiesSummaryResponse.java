package com.kavala.inventory_service.api.rest.dto;

import java.util.List;
import java.util.UUID;

/**
 * Response DTO for copies summary (list with counts).
 */
public record CopiesSummaryResponse(
        UUID bookId,
        List<CopyItem> copies,
        int totalCount,
        int availableCount) {
    /**
     * Individual copy item in the summary.
     */
    public record CopyItem(
            UUID id,
            String barcode,
            String status,
            String statusDescription,
            String shelfLocation,
            boolean available) {
    }

    public double getAvailabilityPercentage() {
        return totalCount > 0 ? (double) availableCount / totalCount * 100.0 : 0.0;
    }

    public boolean hasAvailableCopy() {
        return availableCount > 0;
    }
}
