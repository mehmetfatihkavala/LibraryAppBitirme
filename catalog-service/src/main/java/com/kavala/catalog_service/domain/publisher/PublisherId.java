package com.kavala.catalog_service.domain.publisher;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record PublisherId(UUID value) implements Serializable {

    public PublisherId {
        Objects.requireNonNull(value, "PublisherId cannot be null");
    }

    public static PublisherId of(UUID value) {
        return new PublisherId(value);
    }

    public static PublisherId generate() {
        return new PublisherId(UUID.randomUUID());
    }

}
