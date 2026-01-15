package com.kavala.inventory_service.domain.port;

import com.kavala.inventory_service.domain.model.BookId;

import java.util.Optional;

/**
 * Port for looking up book information from Catalog Service.
 * 
 * This is an Anti-Corruption Layer (ACL) port that:
 * - Provides a clean interface for inventory domain to verify book existence
 * - Decouples inventory service from catalog service implementation details
 * - Enables validation of BookId before creating BookCopy
 * 
 * Implementation options:
 * - Synchronous REST/gRPC call to catalog-service
 * - Event-driven local cache synchronization
 * - Read from eventual consistency local replica
 */
public interface CatalogBookLookupPort {

    /**
     * Checks if a book exists in the catalog.
     *
     * @param bookId the book ID to verify
     * @return true if the book exists in catalog
     */
    boolean bookExists(BookId bookId);

    /**
     * Retrieves basic book information from catalog.
     * Used for validation and enrichment purposes.
     *
     * @param bookId the book ID to look up
     * @return Optional containing book info if found
     */
    Optional<CatalogBookInfo> findBookInfo(BookId bookId);

    /**
     * Validates that a book exists, throwing exception if not.
     *
     * @param bookId the book ID to validate
     * @throws BookNotFoundInCatalogException if book doesn't exist
     */
    default void validateBookExists(BookId bookId) {
        if (!bookExists(bookId)) {
            throw new BookNotFoundInCatalogException(bookId);
        }
    }

    /**
     * Minimal book information from catalog service.
     * Contains only what inventory service needs to know.
     */
    record CatalogBookInfo(
            BookId bookId,
            String title,
            String isbn,
            boolean isActive) {
    }

    /**
     * Exception thrown when a book is not found in the catalog.
     */
    class BookNotFoundInCatalogException extends RuntimeException {
        private final BookId bookId;

        public BookNotFoundInCatalogException(BookId bookId) {
            super(String.format("Book with ID %s not found in catalog", bookId.getValue()));
            this.bookId = bookId;
        }

        public BookId getBookId() {
            return bookId;
        }
    }
}
