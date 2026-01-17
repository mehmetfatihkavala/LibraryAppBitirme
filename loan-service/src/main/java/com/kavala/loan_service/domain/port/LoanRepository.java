package com.kavala.loan_service.domain.port;

import com.kavala.loan_service.domain.model.BookCopyId;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.model.LoanStatus;
import com.kavala.loan_service.domain.model.MemberId;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for Loan aggregate.
 * This is the primary port (driven port) for persistence operations.
 * 
 * Following Hexagonal Architecture principles:
 * - Domain layer defines this interface
 * - Infrastructure layer provides the implementation
 * - Application layer uses this interface for persistence
 */
public interface LoanRepository {

    /**
     * Saves a loan (create or update).
     *
     * @param loan the loan to save
     * @return the saved loan
     */
    Loan save(Loan loan);

    /**
     * Finds a loan by its unique identifier.
     *
     * @param id the loan ID
     * @return an Optional containing the loan if found
     */
    Optional<Loan> findById(LoanId id);

    /**
     * Finds all loans for a specific member.
     *
     * @param memberId the member ID
     * @return list of loans for the given member
     */
    List<Loan> findByMemberId(MemberId memberId);

    /**
     * Finds all loans for a specific book copy.
     *
     * @param bookCopyId the book copy ID
     * @return list of loans for the given book copy
     */
    List<Loan> findByBookCopyId(BookCopyId bookCopyId);

    /**
     * Finds all loans with a specific status.
     *
     * @param status the loan status
     * @return list of loans with the given status
     */
    List<Loan> findByStatus(LoanStatus status);

    /**
     * Finds all open loans (OPEN or OVERDUE status).
     *
     * @return list of active loans
     */
    List<Loan> findAllOpen();

    /**
     * Finds all overdue loans.
     *
     * @return list of overdue loans
     */
    List<Loan> findAllOverdue();

    /**
     * Finds the current active loan for a book copy.
     *
     * @param bookCopyId the book copy ID
     * @return an Optional containing the active loan if exists
     */
    Optional<Loan> findActiveByBookCopyId(BookCopyId bookCopyId);

    /**
     * Counts active loans for a member.
     *
     * @param memberId the member ID
     * @return count of active loans
     */
    long countActiveByMemberId(MemberId memberId);

    /**
     * Finds all loans in the system.
     *
     * @return list of all loans
     */
    List<Loan> findAll();

    /**
     * Checks if a loan exists by ID.
     *
     * @param id the loan ID
     * @return true if exists, false otherwise
     */
    boolean existsById(LoanId id);

    /**
     * Deletes a loan by its ID.
     *
     * @param id the loan ID to delete
     */
    void deleteById(LoanId id);
}
