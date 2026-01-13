package com.kavala.catalog_service.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookSearchResponse(
        UUID id,
        String title,
        CategoryResponse category,
        PublisherResponse publisher,
        LocalDate publishedDate,
        Integer pageCount,
        String language,
        String description,
        List<AuthorResponse> authors) {
}