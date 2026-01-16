package com.kavala.inventory_service.api.rest;

import com.kavala.inventory_service.api.rest.dto.BookCopyResponse;
import com.kavala.inventory_service.api.rest.dto.CopiesSummaryResponse;
import com.kavala.inventory_service.api.rest.dto.CopyAvailabilityResponse;
import com.kavala.inventory_service.application.query.availability.CheckCopyAvailabilityHandler;
import com.kavala.inventory_service.application.query.availability.CheckCopyAvailabilityQuery;
import com.kavala.inventory_service.application.query.get.GetBookCopyHandler;
import com.kavala.inventory_service.application.query.get.GetBookCopyQuery;
import com.kavala.inventory_service.application.query.list.ListCopiesByBookHandler;
import com.kavala.inventory_service.application.query.list.ListCopiesByBookQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

/**
 * REST Controller for BookCopy query operations.
 * Follows CQRS pattern - handles only read operations.
 */
@RestController
@RequestMapping("/api/v1/book-copies")
public class BookCopyQueryController {

    private final GetBookCopyHandler getBookCopyHandler;
    private final ListCopiesByBookHandler listCopiesByBookHandler;
    private final CheckCopyAvailabilityHandler checkCopyAvailabilityHandler;

    public BookCopyQueryController(
            GetBookCopyHandler getBookCopyHandler,
            ListCopiesByBookHandler listCopiesByBookHandler,
            CheckCopyAvailabilityHandler checkCopyAvailabilityHandler) {
        this.getBookCopyHandler = Objects.requireNonNull(getBookCopyHandler);
        this.listCopiesByBookHandler = Objects.requireNonNull(listCopiesByBookHandler);
        this.checkCopyAvailabilityHandler = Objects.requireNonNull(checkCopyAvailabilityHandler);
    }

    /**
     * GET /api/v1/book-copies/{id}
     * Retrieves a single book copy by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookCopyResponse> getBookCopy(@PathVariable UUID id) {
        GetBookCopyQuery.Result result = getBookCopyHandler.handle(GetBookCopyQuery.of(id));

        BookCopyResponse response = new BookCopyResponse(
                result.id(),
                result.bookId(),
                result.barcode(),
                result.status(),
                result.statusDescription(),
                result.shelfLocation(),
                result.isAvailable(),
                result.createdAt(),
                result.updatedAt());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/book-copies/by-book/{bookId}
     * Lists all copies of a specific book.
     */
    @GetMapping("/by-book/{bookId}")
    public ResponseEntity<CopiesSummaryResponse> listCopiesByBook(
            @PathVariable UUID bookId,
            @RequestParam(required = false, defaultValue = "false") Boolean availableOnly) {

        ListCopiesByBookQuery query = ListCopiesByBookQuery.builder()
                .bookId(bookId)
                .availableOnly(availableOnly)
                .build();

        ListCopiesByBookQuery.Result result = listCopiesByBookHandler.handle(query);

        CopiesSummaryResponse response = new CopiesSummaryResponse(
                result.bookId(),
                result.copies().stream()
                        .map(item -> new CopiesSummaryResponse.CopyItem(
                                item.id(),
                                item.barcode(),
                                item.status(),
                                item.statusDescription(),
                                item.shelfLocation(),
                                item.isAvailable()))
                        .toList(),
                result.totalCount(),
                result.availableCount());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/book-copies/availability/{bookId}
     * Checks availability of copies for a specific book.
     */
    @GetMapping("/availability/{bookId}")
    public ResponseEntity<CopyAvailabilityResponse> checkAvailability(@PathVariable UUID bookId) {
        CheckCopyAvailabilityQuery.Result result = checkCopyAvailabilityHandler.handle(
                CheckCopyAvailabilityQuery.of(bookId));

        CopyAvailabilityResponse response = new CopyAvailabilityResponse(
                result.bookId(),
                result.hasAvailableCopy(),
                result.totalCopies(),
                result.availableCopies(),
                result.loanedCopies(),
                result.reservedCopies(),
                result.getAvailabilityPercentage());

        return ResponseEntity.ok(response);
    }
}
