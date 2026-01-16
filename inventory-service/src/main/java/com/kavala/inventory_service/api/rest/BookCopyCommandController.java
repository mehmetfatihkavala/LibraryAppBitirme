package com.kavala.inventory_service.api.rest;

import com.kavala.inventory_service.api.rest.dto.AddBookCopyRequest;
import com.kavala.inventory_service.api.rest.dto.BookCopyResponse;
import com.kavala.inventory_service.api.rest.dto.ChangeCopyStatusRequest;
import com.kavala.inventory_service.api.rest.dto.RelocateBookCopyRequest;
import com.kavala.inventory_service.application.command.add.AddBookCopyCommand;
import com.kavala.inventory_service.application.command.add.AddBookCopyHandler;
import com.kavala.inventory_service.application.command.relocate.RelocateBookCopyCommand;
import com.kavala.inventory_service.application.command.relocate.RelocateBookCopyHandler;
import com.kavala.inventory_service.application.command.remove.RemoveBookCopyCommand;
import com.kavala.inventory_service.application.command.remove.RemoveBookCopyHandler;
import com.kavala.inventory_service.application.command.status.ChangeCopyStatusCommand;
import com.kavala.inventory_service.application.command.status.ChangeCopyStatusHandler;
import com.kavala.inventory_service.application.query.get.GetBookCopyHandler;
import com.kavala.inventory_service.application.query.get.GetBookCopyQuery;
import com.kavala.inventory_service.domain.model.BookCopyId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * REST Controller for BookCopy command operations.
 * Follows CQRS pattern - handles only write operations.
 */
@RestController
@RequestMapping("/api/v1/book-copies")
public class BookCopyCommandController {

    private final AddBookCopyHandler addBookCopyHandler;
    private final ChangeCopyStatusHandler changeCopyStatusHandler;
    private final RelocateBookCopyHandler relocateBookCopyHandler;
    private final RemoveBookCopyHandler removeBookCopyHandler;
    private final GetBookCopyHandler getBookCopyHandler;

    public BookCopyCommandController(
            AddBookCopyHandler addBookCopyHandler,
            ChangeCopyStatusHandler changeCopyStatusHandler,
            RelocateBookCopyHandler relocateBookCopyHandler,
            RemoveBookCopyHandler removeBookCopyHandler,
            GetBookCopyHandler getBookCopyHandler) {
        this.addBookCopyHandler = Objects.requireNonNull(addBookCopyHandler);
        this.changeCopyStatusHandler = Objects.requireNonNull(changeCopyStatusHandler);
        this.relocateBookCopyHandler = Objects.requireNonNull(relocateBookCopyHandler);
        this.removeBookCopyHandler = Objects.requireNonNull(removeBookCopyHandler);
        this.getBookCopyHandler = Objects.requireNonNull(getBookCopyHandler);
    }

    /**
     * POST /api/v1/book-copies
     * Adds a new book copy to the inventory.
     */
    @PostMapping
    public ResponseEntity<BookCopyResponse> addBookCopy(@Valid @RequestBody AddBookCopyRequest request) {
        AddBookCopyCommand command = AddBookCopyCommand.builder()
                .bookId(request.bookId())
                .barcode(request.barcode())
                .shelfLocation(request.shelfLocation())
                .acquiredAt(LocalDateTime.now())
                .build();

        BookCopyId createdId = addBookCopyHandler.handle(command);

        // Fetch the created copy to return full response
        GetBookCopyQuery.Result result = getBookCopyHandler.handle(
                GetBookCopyQuery.of(createdId.getValue()));

        BookCopyResponse response = mapToResponse(result);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdId.getValue())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * PATCH /api/v1/book-copies/{id}/status
     * Changes the status of a book copy.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<BookCopyResponse> changeCopyStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeCopyStatusRequest request) {

        ChangeCopyStatusCommand command = ChangeCopyStatusCommand.builder()
                .bookCopyId(id)
                .newStatus(request.newStatus())
                .reason(request.reason())
                .build();

        changeCopyStatusHandler.handle(command);

        // Fetch updated copy
        GetBookCopyQuery.Result result = getBookCopyHandler.handle(GetBookCopyQuery.of(id));
        return ResponseEntity.ok(mapToResponse(result));
    }

    /**
     * PATCH /api/v1/book-copies/{id}/location
     * Relocates a book copy to a new shelf location.
     */
    @PatchMapping("/{id}/location")
    public ResponseEntity<BookCopyResponse> relocateBookCopy(
            @PathVariable UUID id,
            @RequestBody RelocateBookCopyRequest request) {

        RelocateBookCopyCommand command = RelocateBookCopyCommand.builder()
                .bookCopyId(id)
                .newShelfLocation(request.newShelfLocation())
                .build();

        relocateBookCopyHandler.handle(command);

        // Fetch updated copy
        GetBookCopyQuery.Result result = getBookCopyHandler.handle(GetBookCopyQuery.of(id));
        return ResponseEntity.ok(mapToResponse(result));
    }

    /**
     * DELETE /api/v1/book-copies/{id}
     * Removes a book copy from the inventory.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCopy(
            @PathVariable UUID id,
            @RequestParam(required = false) String reason) {

        RemoveBookCopyCommand command = RemoveBookCopyCommand.builder()
                .bookCopyId(id)
                .reason(reason)
                .build();

        removeBookCopyHandler.handle(command);
    }

    private BookCopyResponse mapToResponse(GetBookCopyQuery.Result result) {
        return new BookCopyResponse(
                result.id(),
                result.bookId(),
                result.barcode(),
                result.status(),
                result.statusDescription(),
                result.shelfLocation(),
                result.isAvailable(),
                result.createdAt(),
                result.updatedAt());
    }
}
