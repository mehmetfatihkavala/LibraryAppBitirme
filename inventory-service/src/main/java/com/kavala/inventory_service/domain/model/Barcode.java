package com.kavala.inventory_service.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing the barcode of a BookCopy.
 * Supports standard library barcode formats (e.g., ISBN-13, custom library
 * codes).
 * Immutable and self-validating.
 */
public final class Barcode {

    private static final Pattern BARCODE_PATTERN = Pattern.compile("^[A-Z0-9\\-]{4,50}$");

    private final String value;

    private Barcode(String value) {
        Objects.requireNonNull(value, "Barcode value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Barcode cannot be blank");
        }
        String normalizedValue = value.trim().toUpperCase();
        if (!BARCODE_PATTERN.matcher(normalizedValue).matches()) {
            throw new IllegalArgumentException(
                    "Barcode must be 4-50 characters long and contain only uppercase letters, numbers, and hyphens");
        }
        this.value = normalizedValue;
    }

    public static Barcode of(String value) {
        return new Barcode(value);
    }

    /**
     * Generates a library-specific barcode with prefix.
     * Format: LIB-{timestamp}-{random}
     */
    public static Barcode generate(String prefix) {
        Objects.requireNonNull(prefix, "Prefix cannot be null");
        String timestamp = String.valueOf(System.currentTimeMillis() % 1000000);
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return new Barcode(prefix.toUpperCase() + "-" + timestamp + "-" + random);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Barcode barcode = (Barcode) o;
        return Objects.equals(value, barcode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
