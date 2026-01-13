package com.kavala.catalog_service.api.rest.dto;

import java.util.UUID;
import java.time.LocalDate;

public record BookResponse(
        UUID id,
        String title,
        UUID categoryId,
        UUID publisherId,
        LocalDate publishedDate,
        Integer pageCount,
        String language) {

}
