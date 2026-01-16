package com.kavala.inventory_service.application.query.availability;

import com.kavala.inventory_service.core.cqrs.QueryHandler;
import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookId;
import com.kavala.inventory_service.domain.model.CopyStatus;
import com.kavala.inventory_service.domain.port.BookCopyQueryPort;
import com.kavala.inventory_service.domain.port.BookCopyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Handler for CheckCopyAvailabilityQuery.
 * Checks and returns availability statistics for a book's copies.
 */
@Service
@Transactional(readOnly = true)
public class CheckCopyAvailabilityHandler
        implements QueryHandler<CheckCopyAvailabilityQuery, CheckCopyAvailabilityQuery.Result> {

    private final BookCopyRepository bookCopyRepository;
    private final BookCopyQueryPort bookCopyQueryPort;

    public CheckCopyAvailabilityHandler(
            BookCopyRepository bookCopyRepository,
            BookCopyQueryPort bookCopyQueryPort) {
        this.bookCopyRepository = Objects.requireNonNull(bookCopyRepository, "BookCopyRepository cannot be null");
        this.bookCopyQueryPort = Objects.requireNonNull(bookCopyQueryPort, "BookCopyQueryPort cannot be null");
    }

    @Override
    public CheckCopyAvailabilityQuery.Result handle(CheckCopyAvailabilityQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        BookId bookId = BookId.of(query.getBookId());

        // Get all copies for the book
        List<BookCopy> allCopies = bookCopyRepository.findByBookId(bookId);

        // Calculate statistics
        long totalCopies = allCopies.size();
        long availableCopies = allCopies.stream()
                .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
                .count();
        long loanedCopies = allCopies.stream()
                .filter(copy -> copy.getStatus() == CopyStatus.LOANED)
                .count();
        long reservedCopies = allCopies.stream()
                .filter(copy -> copy.getStatus() == CopyStatus.RESERVED)
                .count();

        boolean hasAvailable = bookCopyQueryPort.hasAvailableCopy(bookId);

        return new CheckCopyAvailabilityQuery.Result(
                query.getBookId(),
                hasAvailable,
                totalCopies,
                availableCopies,
                loanedCopies,
                reservedCopies);
    }
}
