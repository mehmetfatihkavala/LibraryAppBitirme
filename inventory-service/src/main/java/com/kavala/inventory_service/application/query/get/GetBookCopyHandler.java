package com.kavala.inventory_service.application.query.get;

import com.kavala.inventory_service.core.cqrs.QueryHandler;
import com.kavala.inventory_service.domain.model.BookCopy;
import com.kavala.inventory_service.domain.model.BookCopyId;
import com.kavala.inventory_service.domain.port.BookCopyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Handler for GetBookCopyQuery.
 * Retrieves a single book copy and maps it to the query result.
 */
@Service
@Transactional(readOnly = true)
public class GetBookCopyHandler implements QueryHandler<GetBookCopyQuery, GetBookCopyQuery.Result> {

    private final BookCopyRepository bookCopyRepository;

    public GetBookCopyHandler(BookCopyRepository bookCopyRepository) {
        this.bookCopyRepository = Objects.requireNonNull(bookCopyRepository, "BookCopyRepository cannot be null");
    }

    @Override
    public GetBookCopyQuery.Result handle(GetBookCopyQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        BookCopyId copyId = BookCopyId.of(query.getBookCopyId());

        BookCopy bookCopy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new BookCopyNotFoundException(copyId));

        return mapToResult(bookCopy);
    }

    private GetBookCopyQuery.Result mapToResult(BookCopy bookCopy) {
        return new GetBookCopyQuery.Result(
                bookCopy.getId().getValue(),
                bookCopy.getBookId().getValue(),
                bookCopy.getBarcode().getValue(),
                bookCopy.getStatus().name(),
                bookCopy.getStatus().getDescription(),
                bookCopy.getShelfLocation()
                        .map(loc -> loc.getFullLocation())
                        .orElse(null),
                bookCopy.isAvailable(),
                bookCopy.getAuditInfo().getCreatedAt(),
                bookCopy.getAuditInfo().getUpdatedAt().orElse(null));
    }

    /**
     * Exception thrown when a book copy is not found.
     */
    public static class BookCopyNotFoundException extends RuntimeException {
        private final BookCopyId bookCopyId;

        public BookCopyNotFoundException(BookCopyId bookCopyId) {
            super(String.format("Book copy with ID %s not found", bookCopyId.getValue()));
            this.bookCopyId = bookCopyId;
        }

        public BookCopyId getBookCopyId() {
            return bookCopyId;
        }
    }
}
