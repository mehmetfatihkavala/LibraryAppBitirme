package com.kavala.loan_service.infrastructure.adapter.http.inventory;

import com.kavala.loan_service.domain.model.BookCopyId;
import com.kavala.loan_service.domain.port.InventoryCopyPort;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Adapter implementing InventoryCopyPort using HTTP client.
 */
@Component
public class InventoryCopyAdapter implements InventoryCopyPort {

    private final InventoryClient inventoryClient;

    public InventoryCopyAdapter(InventoryClient inventoryClient) {
        this.inventoryClient = Objects.requireNonNull(inventoryClient, "InventoryClient cannot be null");
    }

    @Override
    public boolean copyExists(BookCopyId bookCopyId) {
        try {
            return inventoryClient.copyExists(bookCopyId.getValue());
        } catch (Exception e) {
            // Log and return false on error
            return false;
        }
    }

    @Override
    public boolean isAvailable(BookCopyId bookCopyId) {
        try {
            return inventoryClient.isAvailable(bookCopyId.getValue());
        } catch (Exception e) {
            // Log and return false on error
            return false;
        }
    }

    @Override
    public void validateAvailability(BookCopyId bookCopyId) {
        if (!copyExists(bookCopyId)) {
            throw new BookCopyNotFoundException(bookCopyId);
        }
        if (!isAvailable(bookCopyId)) {
            throw new BookCopyNotAvailableException(bookCopyId, "Copy is not available for loan");
        }
    }

    @Override
    public void markAsLoaned(BookCopyId bookCopyId) {
        try {
            inventoryClient.markAsLoaned(bookCopyId.getValue());
        } catch (Exception e) {
            // Log error but don't throw - this is a best-effort update
        }
    }

    @Override
    public void markAsReturned(BookCopyId bookCopyId) {
        try {
            inventoryClient.markAsReturned(bookCopyId.getValue());
        } catch (Exception e) {
            // Log error but don't throw - this is a best-effort update
        }
    }
}
