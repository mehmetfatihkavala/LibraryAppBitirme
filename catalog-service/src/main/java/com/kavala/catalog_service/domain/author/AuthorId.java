package com.kavala.catalog_service.domain.author;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record AuthorId(UUID value) implements Serializable {

    public AuthorId {
        Objects.requireNonNull(value, "AuthorId cannot be null");
    }

    public static AuthorId of(UUID value) {
        return new AuthorId(value);
    }

    public static AuthorId generate() {
        return new AuthorId(UUID.randomUUID());
    }

}
