package com.kavala.inventory_service.application.query.list;

import com.kavala.inventory_service.core.cqrs.QueryHandler;
import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookId;
import com.kavala.inventory_service.domain.port.BookCopyQueryPort;
import com.kavala.inventory_service.domain.port.BookCopyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Handler for ListCopiesByBookQuery.
 * Lists all copies of a specific book with optional filtering.
 */
@Service
@Transactional(readOnly = true)
public class ListCopiesByBookHandler implements QueryHandler<ListCopiesByBookQuery, ListCopiesByBookQuery.Result> {

    private final BookCopyRepository bookCopyRepository;
    private final BookCopyQueryPort bookCopyQueryPort;

    public ListCopiesByBookHandler(
            BookCopyRepository bookCopyRepository,
            BookCopyQueryPort bookCopyQueryPort) {
        this.bookCopyRepository = Objects.requireNonNull(bookCopyRepository, "BookCopyRepository cannot be null");
        this.bookCopyQueryPort = Objects.requireNonNull(bookCopyQueryPort, "BookCopyQueryPort cannot be null");
    }

    @Override
    public ListCopiesByBookQuery.Result handle(ListCopiesByBookQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        BookId bookId = BookId.of(query.getBookId());

        // Get copies based on filter
        List<BookCopy> copies;
        if (query.isAvailableOnlyFilter()) {
            copies = bookCopyQueryPort.findAvailableCopiesByBookId(bookId);
        } else {
            copies = bookCopyRepository.findByBookId(bookId);
        }

        // Map to result
        List<ListCopiesByBookQuery.CopyItem> copyItems = copies.stream()
                .map(this::mapToCopyItem)
                .toList();

        // Calculate stats
        int availableCount = (int) copies.stream()
                .filter(BookCopy::isAvailable)
                .count();

        return new ListCopiesByBookQuery.Result(
                query.getBookId(),
                copyItems,
                copies.size(),
                availableCount);
    }

    private ListCopiesByBookQuery.CopyItem mapToCopyItem(BookCopy bookCopy) {
        return new ListCopiesByBookQuery.CopyItem(
                bookCopy.getId().getValue(),
                bookCopy.getBarcode().getValue(),
                bookCopy.getStatus().name(),
                bookCopy.getStatus().getDescription(),
                bookCopy.getShelfLocation()
                        .map(loc -> loc.getFullLocation())
                        .orElse(null),
                bookCopy.isAvailable());
    }
}
