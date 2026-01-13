package com.kavala.catalog_service.domain.book.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record BookId(UUID value) implements Serializable {

    public BookId {
        Objects.requireNonNull(value, "BookId cannot be null");
    }

    public static BookId of(UUID value) {
        return new BookId(value);
    }

    public static BookId generate() {
        return new BookId(UUID.randomUUID());
    }

}
