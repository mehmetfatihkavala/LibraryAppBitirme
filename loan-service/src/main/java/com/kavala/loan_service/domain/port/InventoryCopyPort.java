package com.kavala.loan_service.domain.port;

import com.kavala.loan_service.domain.model.BookCopyId;

/**
 * Driven port for inventory copy operations.
 * This port abstracts the communication with inventory-service.
 * 
 * Following Hexagonal Architecture:
 * - Domain layer defines this interface
 * - Infrastructure layer provides HTTP client implementation
 */
public interface InventoryCopyPort {

    /**
     * Checks if a book copy exists in the inventory.
     *
     * @param bookCopyId the book copy ID to check
     * @return true if copy exists
     */
    boolean copyExists(BookCopyId bookCopyId);

    /**
     * Checks if a book copy is available for loan.
     *
     * @param bookCopyId the book copy ID to check
     * @return true if copy is available
     */
    boolean isAvailable(BookCopyId bookCopyId);

    /**
     * Validates that a book copy exists and is available.
     *
     * @param bookCopyId the book copy ID to validate
     * @throws BookCopyNotFoundException     if copy does not exist
     * @throws BookCopyNotAvailableException if copy is not available
     */
    void validateAvailability(BookCopyId bookCopyId);

    /**
     * Marks a book copy as loaned in the inventory.
     *
     * @param bookCopyId the book copy ID to mark
     */
    void markAsLoaned(BookCopyId bookCopyId);

    /**
     * Marks a book copy as returned (available) in the inventory.
     *
     * @param bookCopyId the book copy ID to mark
     */
    void markAsReturned(BookCopyId bookCopyId);

    /**
     * Exception thrown when book copy is not found.
     */
    class BookCopyNotFoundException extends RuntimeException {
        private final BookCopyId bookCopyId;

        public BookCopyNotFoundException(BookCopyId bookCopyId) {
            super(String.format("Book copy not found: %s", bookCopyId));
            this.bookCopyId = bookCopyId;
        }

        public BookCopyId getBookCopyId() {
            return bookCopyId;
        }
    }

    /**
     * Exception thrown when book copy is not available for loan.
     */
    class BookCopyNotAvailableException extends RuntimeException {
        private final BookCopyId bookCopyId;
        private final String reason;

        public BookCopyNotAvailableException(BookCopyId bookCopyId, String reason) {
            super(String.format("Book copy %s is not available: %s", bookCopyId, reason));
            this.bookCopyId = bookCopyId;
            this.reason = reason;
        }

        public BookCopyId getBookCopyId() {
            return bookCopyId;
        }

        public String getReason() {
            return reason;
        }
    }
}
