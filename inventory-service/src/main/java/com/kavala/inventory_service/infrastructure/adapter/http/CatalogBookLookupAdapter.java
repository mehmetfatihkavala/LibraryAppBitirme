package com.kavala.inventory_service.infrastructure.adapter.http;

import com.kavala.inventory_service.domain.model.BookId;
import com.kavala.inventory_service.domain.port.CatalogBookLookupPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

/**
 * HTTP adapter for CatalogBookLookupPort.
 * Communicates with catalog-service to verify book existence.
 * 
 * Implements Anti-Corruption Layer pattern for cross-service communication.
 * Uses RestClient for synchronous HTTP calls (Spring Boot 3+).
 */
@Component
public class CatalogBookLookupAdapter implements CatalogBookLookupPort {

    private static final Logger log = LoggerFactory.getLogger(CatalogBookLookupAdapter.class);

    private final RestClient restClient;

    public CatalogBookLookupAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${catalog.service.url:http://localhost:8081}") String catalogServiceUrl) {
        this.restClient = restClientBuilder
                .baseUrl(catalogServiceUrl)
                .build();
    }

    @Override
    public boolean bookExists(BookId bookId) {
        try {
            CatalogBookDto response = restClient.get()
                    .uri("/api/v1/books/{id}", bookId.getValue())
                    .retrieve()
                    .body(CatalogBookDto.class);

            return response != null;
        } catch (HttpClientErrorException.NotFound e) {
            log.debug("Book not found in catalog: {}", bookId.getValue());
            return false;
        } catch (RestClientException e) {
            log.error("Error checking book existence in catalog: {}", bookId.getValue(), e);
            // Fail-safe: if catalog service is down, we don't block operations
            // In production, consider circuit breaker pattern
            return true;
        }
    }

    @Override
    public Optional<CatalogBookInfo> findBookInfo(BookId bookId) {
        try {
            CatalogBookDto response = restClient.get()
                    .uri("/api/v1/books/{id}", bookId.getValue())
                    .retrieve()
                    .body(CatalogBookDto.class);

            if (response == null) {
                return Optional.empty();
            }

            return Optional.of(new CatalogBookInfo(
                    bookId,
                    response.title(),
                    response.isbn(),
                    response.active()));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            log.error("Error fetching book info from catalog: {}", bookId.getValue(), e);
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("Error fetching book info from catalog: {}", bookId.getValue(), e);
            return Optional.empty();
        }
    }

    /**
     * DTO for catalog service response.
     */
    record CatalogBookDto(
            String id,
            String title,
            String isbn,
            boolean active) {
    }
}
