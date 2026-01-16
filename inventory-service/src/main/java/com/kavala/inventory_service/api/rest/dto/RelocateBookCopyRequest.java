package com.kavala.inventory_service.api.rest.dto;

/**
 * Request DTO for relocating a book copy to a new shelf location.
 */
public record RelocateBookCopyRequest(
        String newShelfLocation // Format: "FLOOR-SECTION-SHELF[-POSITION]" or null to clear
) {
    public boolean isClearLocationRequest() {
        return newShelfLocation == null || newShelfLocation.isBlank();
    }
}
