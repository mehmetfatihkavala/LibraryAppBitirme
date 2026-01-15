package com.kavala.inventory_service.domain.port;

import com.kavala.inventory_service.domain.model.Barcode;
import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookCopyId;
import com.kavala.inventory_service.domain.model.BookId;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for BookCopy aggregate.
 * This is the primary port (driven port) for persistence operations.
 * 
 * Following Hexagonal Architecture principles:
 * - Domain layer defines this interface
 * - Infrastructure layer provides the implementation
 * - Application layer uses this interface for persistence
 */
public interface BookCopyRepository {

    /**
     * Saves a book copy (create or update).
     *
     * @param bookCopy the book copy to save
     * @return the saved book copy
     */
    BookCopy save(BookCopy bookCopy);

    /**
     * Finds a book copy by its unique identifier.
     *
     * @param id the book copy ID
     * @return an Optional containing the book copy if found
     */
    Optional<BookCopy> findById(BookCopyId id);

    /**
     * Finds a book copy by its barcode.
     *
     * @param barcode the unique barcode
     * @return an Optional containing the book copy if found
     */
    Optional<BookCopy> findByBarcode(Barcode barcode);

    /**
     * Finds all copies of a specific book.
     *
     * @param bookId the book ID from catalog service
     * @return list of book copies for the given book
     */
    List<BookCopy> findByBookId(BookId bookId);

    /**
     * Finds all book copies in the system.
     *
     * @return list of all book copies
     */
    List<BookCopy> findAll();

    /**
     * Deletes a book copy by its ID.
     *
     * @param id the book copy ID to delete
     */
    void deleteById(BookCopyId id);

    /**
     * Deletes a book copy.
     *
     * @param bookCopy the book copy to delete
     */
    void delete(BookCopy bookCopy);

    /**
     * Checks if a book copy exists by ID.
     *
     * @param id the book copy ID
     * @return true if exists, false otherwise
     */
    boolean existsById(BookCopyId id);

    /**
     * Checks if a book copy exists with the given barcode.
     *
     * @param barcode the barcode to check
     * @return true if exists, false otherwise
     */
    boolean existsByBarcode(Barcode barcode);

    /**
     * Counts total book copies for a specific book.
     *
     * @param bookId the book ID
     * @return count of copies
     */
    long countByBookId(BookId bookId);
}
