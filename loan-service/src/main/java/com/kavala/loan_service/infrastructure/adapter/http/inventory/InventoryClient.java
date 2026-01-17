package com.kavala.loan_service.infrastructure.adapter.http.inventory;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.UUID;

/**
 * HTTP client for inventory-service.
 */
@HttpExchange("/api/book-copies")
public interface InventoryClient {

    /**
     * Checks if a book copy exists.
     */
    @GetExchange("/{copyId}/exists")
    boolean copyExists(@PathVariable UUID copyId);

    /**
     * Checks if a book copy is available for loan.
     */
    @GetExchange("/{copyId}/available")
    boolean isAvailable(@PathVariable UUID copyId);

    /**
     * Marks a book copy as loaned.
     */
    @PostExchange("/{copyId}/mark-loaned")
    void markAsLoaned(@PathVariable UUID copyId);

    /**
     * Marks a book copy as returned (available).
     */
    @PostExchange("/{copyId}/mark-returned")
    void markAsReturned(@PathVariable UUID copyId);
}
