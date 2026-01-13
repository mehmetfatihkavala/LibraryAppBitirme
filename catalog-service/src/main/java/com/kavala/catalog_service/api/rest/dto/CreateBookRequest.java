package com.kavala.catalog_service.api.rest.dto;

import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBookRequest(
        @NotBlank String title,
        @NotNull UUID categoryId,
        @NotNull UUID publisherId,
        LocalDate publishedDate,
        @Positive Integer pageCount,
        @NotBlank String language,
        String description,
        @NotEmpty List<UUID> authorIds) {

}
