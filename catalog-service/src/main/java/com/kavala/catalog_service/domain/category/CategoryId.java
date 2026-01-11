package com.kavala.catalog_service.domain.category;

import java.util.Objects;
import java.util.UUID;

public record CategoryId(UUID value) {

    public CategoryId {
        Objects.requireNonNull(value, "CategoryId cannot be null");
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID());
    }

}
