package com.kavala.inventory_service.api.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for adding a new book copy.
 */
public record AddBookCopyRequest(
        @NotNull(message = "Book ID is required") UUID bookId,

        @NotBlank(message = "Barcode is required") String barcode,

        String shelfLocation // Optional: Format "FLOOR-SECTION-SHELF[-POSITION]"
) {
}
