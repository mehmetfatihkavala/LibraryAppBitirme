package com.kavala.catalog_service.api.rest.dto;

import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

public record BookDetailResponse(
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
