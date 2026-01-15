package com.kavala.inventory_service.domain.port;

import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookId;
import com.kavala.inventory_service.domain.model.CopyStatus;
import com.kavala.inventory_service.domain.model.ShelfLocation;

import java.util.List;

/**
 * Query port for read-only operations on BookCopy aggregate.
 * 
 * Following CQRS principles:
 * - This port is dedicated to query (read) operations
 * - Supports optimized read models and projections
 * - Can be backed by different storage for read optimization
 * 
 * This separation allows:
 * - Independent scaling of read and write operations
 * - Optimized queries without affecting command operations
 * - Different consistency models for reads vs writes
 */
public interface BookCopyQueryPort {

    /**
     * Finds all available copies for a specific book.
     *
     * @param bookId the book ID
     * @return list of available book copies
     */
    List<BookCopy> findAvailableCopiesByBookId(BookId bookId);

    /**
     * Finds all book copies with a specific status.
     *
     * @param status the status to filter by
     * @return list of book copies with the given status
     */
    List<BookCopy> findByStatus(CopyStatus status);

    /**
     * Finds all book copies at a specific shelf location.
     *
     * @param shelfLocation the shelf location
     * @return list of book copies at the location
     */
    List<BookCopy> findByShelfLocation(ShelfLocation shelfLocation);

    /**
     * Finds all book copies in a specific section.
     *
     * @param section the section identifier (e.g., "A", "B")
     * @return list of book copies in the section
     */
    List<BookCopy> findBySection(String section);

    /**
     * Counts available copies for a specific book.
     *
     * @param bookId the book ID
     * @return count of available copies
     */
    long countAvailableCopiesByBookId(BookId bookId);

    /**
     * Counts all copies with a specific status.
     *
     * @param status the status to count
     * @return count of copies with the status
     */
    long countByStatus(CopyStatus status);

    /**
     * Checks if any available copy exists for a book.
     *
     * @param bookId the book ID
     * @return true if at least one available copy exists
     */
    boolean hasAvailableCopy(BookId bookId);

    /**
     * Finds copies without shelf location (for reshelving).
     *
     * @return list of book copies without assigned shelf location
     */
    List<BookCopy> findCopiesWithoutShelfLocation();

    /**
     * Finds recently acquired copies.
     *
     * @param limit maximum number of results
     * @return list of recently acquired book copies
     */
    List<BookCopy> findRecentlyAcquired(int limit);

    /**
     * Searches copies by partial barcode match.
     *
     * @param barcodePattern partial barcode pattern
     * @return list of matching book copies
     */
    List<BookCopy> searchByBarcodePattern(String barcodePattern);
}
