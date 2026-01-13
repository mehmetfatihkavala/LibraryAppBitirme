package com.kavala.catalog_service.application.query.author;

import java.util.UUID;

import com.kavala.catalog_service.api.rest.dto.AuthorResponse;
import com.kavala.catalog_service.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

public record GetAuthorQuery(
        @NotNull UUID authorId) implements Query<AuthorResponse> {

}
