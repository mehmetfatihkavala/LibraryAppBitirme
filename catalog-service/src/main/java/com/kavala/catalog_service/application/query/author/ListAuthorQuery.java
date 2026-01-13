package com.kavala.catalog_service.application.query.author;

import java.util.List;

import com.kavala.catalog_service.api.rest.dto.AuthorResponse;
import com.kavala.catalog_service.core.cqrs.Query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ListAuthorQuery(
        @Min(0) int page,
        @Min(1) @Max(100) int size) implements Query<List<AuthorResponse>> {

}
