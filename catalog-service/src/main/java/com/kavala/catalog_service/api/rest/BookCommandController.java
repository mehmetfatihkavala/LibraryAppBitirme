package com.kavala.catalog_service.api.rest;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kavala.catalog_service.api.rest.dto.CreateBookRequest;
import com.kavala.catalog_service.api.rest.dto.UpdateBookRequest;
import com.kavala.catalog_service.application.command.book.create.CreateBookCommand;
import com.kavala.catalog_service.application.command.book.create.CreateBookCommandHandler;
import com.kavala.catalog_service.application.command.book.delete.DeleteBookCommand;
import com.kavala.catalog_service.application.command.book.delete.DeleteBookCommandHandler;
import com.kavala.catalog_service.application.command.book.update.UpdateBookCommand;
import com.kavala.catalog_service.application.command.book.update.UpdateBookCommandHandler;

import jakarta.validation.Valid;

/**
 * Kitap yazma işlemlerini yöneten REST Controller.
 * CQRS pattern'ine uygun olarak sadece Command (Create, Update, Delete)
 * işlemlerini içerir.
 * Query işlemleri BookQueryController'da yer almaktadır.
 * 
 * @see BookQueryController - Okuma işlemleri için
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookCommandController {

    private final CreateBookCommandHandler createBookCommandHandler;
    private final UpdateBookCommandHandler updateBookCommandHandler;
    private final DeleteBookCommandHandler deleteBookCommandHandler;

    public BookCommandController(
            CreateBookCommandHandler createBookCommandHandler,
            UpdateBookCommandHandler updateBookCommandHandler,
            DeleteBookCommandHandler deleteBookCommandHandler) {
        this.createBookCommandHandler = createBookCommandHandler;
        this.updateBookCommandHandler = updateBookCommandHandler;
        this.deleteBookCommandHandler = deleteBookCommandHandler;
    }

    /**
     * Yeni bir kitap oluşturur.
     * 
     * İstek gövdesinde title, categoryId, publisherId, publishedDate, pageCount,
     * language, description ve authorIds alanları beklenir.
     * 
     * @param request Kitap oluşturma isteği (CreateBookRequest)
     * @return 201 Created - Oluşturulan kitabın URI'si ile birlikte
     * @throws IllegalArgumentException Category, Publisher veya Author bulunamazsa
     */
    @PostMapping
    public ResponseEntity<UUID> createBook(@Valid @RequestBody CreateBookRequest request) {
        CreateBookCommand command = new CreateBookCommand(
                request.title(),
                request.categoryId(),
                request.publisherId(),
                request.publishedDate(),
                request.pageCount(),
                request.language(),
                request.description(),
                request.authorIds());

        UUID bookId = createBookCommandHandler.handle(command);

        return ResponseEntity
                .created(URI.create("/api/v1/books/" + bookId))
                .body(bookId);
    }

    /**
     * Mevcut bir kitabı günceller.
     * 
     * Tüm alanların güncellenmesi beklenir (tam güncelleme - PUT semantiği).
     * Kısmi güncelleme için PATCH endpoint'i tercih edilebilir.
     * 
     * @param id      Güncellenecek kitabın UUID'si
     * @param request Kitap güncelleme isteği (UpdateBookRequest)
     * @return 204 No Content - Başarılı güncelleme
     * @throws IllegalArgumentException Kitap, Category, Publisher veya Author
     *                                  bulunamazsa
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookRequest request) {

        UpdateBookCommand command = new UpdateBookCommand(
                id,
                request.title(),
                request.categoryId(),
                request.publisherId(),
                request.publishedDate(),
                request.pageCount(),
                request.language(),
                request.description(),
                request.authorIds());

        updateBookCommandHandler.handle(command);

        return ResponseEntity.noContent().build();
    }

    /**
     * Bir kitabı siler.
     * 
     * Silme işlemi geri alınamaz. Domain kurallarına göre soft-delete
     * veya hard-delete uygulanabilir.
     * 
     * @param id Silinecek kitabın UUID'si
     * @return 204 No Content - Başarılı silme
     * @throws IllegalArgumentException Kitap bulunamazsa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        DeleteBookCommand command = new DeleteBookCommand(id);

        deleteBookCommandHandler.handle(command);

        return ResponseEntity.noContent().build();
    }
}
