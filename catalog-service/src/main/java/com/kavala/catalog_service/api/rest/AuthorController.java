package com.kavala.catalog_service.api.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kavala.catalog_service.api.rest.dto.AuthorResponse;
import com.kavala.catalog_service.api.rest.dto.CreateAuthorRequest;
import com.kavala.catalog_service.application.command.author.CreateAuthorCommand;
import com.kavala.catalog_service.application.command.author.CreateAuthorCommandHandler;
import com.kavala.catalog_service.application.query.author.GetAuthorQuery;
import com.kavala.catalog_service.application.query.author.GetAuthorQueryHandler;
import com.kavala.catalog_service.application.query.author.ListAuthorQuery;
import com.kavala.catalog_service.application.query.author.ListAuthorQueryHandler;

import jakarta.validation.Valid;

/**
 * Yazar (Author) işlemlerini yöneten REST Controller.
 * 
 * Bu controller hem Command hem de Query işlemlerini içerir.
 * Küçük entity'ler için ayrı controller yerine tek controller tercih
 * edilmiştir.
 * CQRS pattern'i handler seviyesinde uygulanmaya devam eder.
 */
@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final CreateAuthorCommandHandler createAuthorCommandHandler;
    private final GetAuthorQueryHandler getAuthorQueryHandler;
    private final ListAuthorQueryHandler listAuthorQueryHandler;

    public AuthorController(
            CreateAuthorCommandHandler createAuthorCommandHandler,
            GetAuthorQueryHandler getAuthorQueryHandler,
            ListAuthorQueryHandler listAuthorQueryHandler) {
        this.createAuthorCommandHandler = createAuthorCommandHandler;
        this.getAuthorQueryHandler = getAuthorQueryHandler;
        this.listAuthorQueryHandler = listAuthorQueryHandler;
    }

    /**
     * Yeni bir yazar oluşturur.
     * 
     * İstek gövdesinde name, birthDate ve nationality alanları beklenir.
     * Yazar adı boş olamaz ve maksimum 255 karakter olmalıdır.
     * 
     * @param request Yazar oluşturma isteği (CreateAuthorRequest)
     * @return 201 Created - Oluşturulan yazarın UUID'si ve URI'si
     */
    @PostMapping
    public ResponseEntity<UUID> createAuthor(@Valid @RequestBody CreateAuthorRequest request) {
        CreateAuthorCommand command = new CreateAuthorCommand(
                request.name(),
                request.birthDate(),
                request.nationality());

        UUID authorId = createAuthorCommandHandler.handle(command);

        return ResponseEntity
                .created(URI.create("/api/v1/authors/" + authorId))
                .body(authorId);
    }

    /**
     * Belirtilen ID'ye sahip yazarın bilgilerini getirir.
     * 
     * @param id Yazar UUID'si
     * @return 200 OK - Yazar bilgileri (AuthorResponse)
     * @throws IllegalArgumentException Yazar bulunamazsa
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable UUID id) {
        GetAuthorQuery query = new GetAuthorQuery(id);

        AuthorResponse response = getAuthorQueryHandler.handle(query);

        return ResponseEntity.ok(response);
    }

    /**
     * Tüm yazarları sayfalı olarak listeler.
     * 
     * @param page Sayfa numarası (0'dan başlar, varsayılan: 0)
     * @param size Sayfa başına kayıt sayısı (1-100 arası, varsayılan: 20)
     * @return 200 OK - Yazar listesi (List<AuthorResponse>)
     */
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> listAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ListAuthorQuery query = new ListAuthorQuery(page, size);

        List<AuthorResponse> response = listAuthorQueryHandler.handle(query);

        return ResponseEntity.ok(response);
    }
}
