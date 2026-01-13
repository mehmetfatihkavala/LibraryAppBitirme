package com.kavala.catalog_service.application.query.book.search;

import java.util.List;

import com.kavala.catalog_service.api.rest.dto.BookResponse;
import com.kavala.catalog_service.core.cqrs.Query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SearchBooksQuery(
        @NotBlank @Size(max = 200) String text,
        @Min(0) int page,
        @Min(1) @Max(100) int size) implements Query<List<BookResponse>> {

}
