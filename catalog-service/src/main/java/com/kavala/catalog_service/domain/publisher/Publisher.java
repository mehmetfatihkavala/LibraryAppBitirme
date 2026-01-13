package com.kavala.catalog_service.domain.publisher;

import java.time.Instant;
import java.util.Objects;

public class Publisher {

    private final PublisherId publisherId;
    private final String name;
    private final Instant createdAt;

    private Publisher(PublisherId publisherId, String name, Instant createdAt) {
        this.publisherId = publisherId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static Publisher createPublisher(PublisherId publisherId, String name) {
        validateName(name);
        return new Publisher(publisherId, name, Instant.now());
    }

    public static Publisher rehydrate(PublisherId publisherId, String name, Instant createdAt) {
        return new Publisher(publisherId, name, createdAt);
    }

    public static Publisher updatePublisher(Publisher publisher, String name) {
        validateName(name);
        return new Publisher(publisher.getPublisherId(), name, publisher.getCreatedAt());
    }

    private static void validateName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Name cannot be longer than 255 characters");
        }
    }

    public PublisherId getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Publisher other = (Publisher) obj;
        return Objects.equals(publisherId, other.publisherId);
    }

}
