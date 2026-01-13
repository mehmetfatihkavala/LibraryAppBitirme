package com.kavala.catalog_service.api.rest.dto;

import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Positive;

public record UpdateBookRequest(
        String title,
        UUID categoryId,
        UUID publisherId,
        LocalDate publishedDate,
        @Positive Integer pageCount,
        String language,
        String description,
        List<UUID> authorIds) {

}
