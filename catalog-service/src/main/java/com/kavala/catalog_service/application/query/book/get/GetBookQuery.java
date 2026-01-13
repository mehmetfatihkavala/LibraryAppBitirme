package com.kavala.catalog_service.application.query.book.get;

import java.util.UUID;

import com.kavala.catalog_service.api.rest.dto.BookDetailResponse;
import com.kavala.catalog_service.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

public record GetBookQuery(
        @NotNull UUID bookId) implements Query<BookDetailResponse> {

}
