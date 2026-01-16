package com.kavala.inventory_service.api.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for book copy details.
 */
public record BookCopyResponse(
        UUID id,
        UUID bookId,
        String barcode,
        String status,
        String statusDescription,
        String shelfLocation,
        boolean available,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    /**
     * Creates a response from query result.
     */
    public static BookCopyResponse from(
            UUID id,
            UUID bookId,
            String barcode,
            String status,
            String statusDescription,
            String shelfLocation,
            boolean available,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return new BookCopyResponse(
                id, bookId, barcode, status, statusDescription,
                shelfLocation, available, createdAt, updatedAt);
    }
}
