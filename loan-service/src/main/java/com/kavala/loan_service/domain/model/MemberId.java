package com.kavala.loan_service.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing a reference to a Member from member-service.
 * Immutable and self-validating.
 */
public final class MemberId {

    private final UUID value;

    private MemberId(UUID value) {
        this.value = Objects.requireNonNull(value, "MemberId value cannot be null");
    }

    public static MemberId of(UUID value) {
        return new MemberId(value);
    }

    public static MemberId of(String value) {
        Objects.requireNonNull(value, "MemberId string value cannot be null");
        return new MemberId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MemberId that = (MemberId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
