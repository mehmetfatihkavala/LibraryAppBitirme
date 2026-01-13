package com.kavala.catalog_service.api.rest;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kavala.catalog_service.api.rest.dto.CreatePublisherRequest;
import com.kavala.catalog_service.application.command.publisher.CreatePublisherCommand;
import com.kavala.catalog_service.application.command.publisher.CreatePublisherCommandHandler;

import jakarta.validation.Valid;

/**
 * Yayıncı (Publisher) işlemlerini yöneten REST Controller.
 * 
 * Şu anki implementasyonda sadece Create işlemi mevcuttur.
 * İhtiyaç durumunda Get, List, Update ve Delete işlemleri eklenebilir.
 * CQRS pattern'i handler seviyesinde uygulanmaktadır.
 */
@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController {

    private final CreatePublisherCommandHandler createPublisherCommandHandler;

    public PublisherController(CreatePublisherCommandHandler createPublisherCommandHandler) {
        this.createPublisherCommandHandler = createPublisherCommandHandler;
    }

    /**
     * Yeni bir yayıncı oluşturur.
     * 
     * İstek gövdesinde name alanı beklenir.
     * Yayıncı adı boş olamaz ve maksimum 255 karakter olmalıdır.
     * 
     * @param request Yayıncı oluşturma isteği (CreatePublisherRequest)
     * @return 201 Created - Oluşturulan yayıncının UUID'si ve URI'si
     */
    @PostMapping
    public ResponseEntity<UUID> createPublisher(@Valid @RequestBody CreatePublisherRequest request) {
        CreatePublisherCommand command = new CreatePublisherCommand(request.name());

        UUID publisherId = createPublisherCommandHandler.handle(command);

        return ResponseEntity
                .created(URI.create("/api/v1/publishers/" + publisherId))
                .body(publisherId);
    }
}
