package com.kavala.loan_service.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing the unique identifier of a Loan.
 * Immutable and self-validating.
 */
public final class LoanId {

    private final UUID value;

    private LoanId(UUID value) {
        this.value = Objects.requireNonNull(value, "LoanId value cannot be null");
    }

    public static LoanId of(UUID value) {
        return new LoanId(value);
    }

    public static LoanId of(String value) {
        Objects.requireNonNull(value, "LoanId string value cannot be null");
        return new LoanId(UUID.fromString(value));
    }

    public static LoanId generate() {
        return new LoanId(UUID.randomUUID());
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
        LoanId that = (LoanId) o;
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
