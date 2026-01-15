package com.kavala.inventory_service.domain.model;

import java.util.Objects;
import java.util.Optional;

/**
 * Value Object representing the physical shelf location of a BookCopy.
 * Optional - a book copy may not have an assigned location yet.
 * Format: FLOOR-SECTION-SHELF-POSITION (e.g., "2-A-5-12")
 * Immutable and self-validating.
 */
public final class ShelfLocation {

    private final String floor;
    private final String section;
    private final String shelf;
    private final String position;

    private ShelfLocation(String floor, String section, String shelf, String position) {
        this.floor = validatePart(floor, "Floor");
        this.section = validatePart(section, "Section");
        this.shelf = validatePart(shelf, "Shelf");
        this.position = position != null ? position.trim() : null; // Position is optional
    }

    private static String validatePart(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        if (trimmed.length() > 10) {
            throw new IllegalArgumentException(fieldName + " cannot exceed 10 characters");
        }
        return trimmed.toUpperCase();
    }

    public static ShelfLocation of(String floor, String section, String shelf) {
        return new ShelfLocation(floor, section, shelf, null);
    }

    public static ShelfLocation of(String floor, String section, String shelf, String position) {
        return new ShelfLocation(floor, section, shelf, position);
    }

    /**
     * Parse from formatted string (e.g., "2-A-5-12" or "2-A-5")
     */
    public static ShelfLocation parse(String locationString) {
        Objects.requireNonNull(locationString, "Location string cannot be null");
        String[] parts = locationString.split("-");
        if (parts.length < 3 || parts.length > 4) {
            throw new IllegalArgumentException(
                    "Invalid location format. Expected: FLOOR-SECTION-SHELF or FLOOR-SECTION-SHELF-POSITION");
        }
        String position = parts.length == 4 ? parts[3] : null;
        return new ShelfLocation(parts[0], parts[1], parts[2], position);
    }

    public String getFloor() {
        return floor;
    }

    public String getSection() {
        return section;
    }

    public String getShelf() {
        return shelf;
    }

    public Optional<String> getPosition() {
        return Optional.ofNullable(position);
    }

    /**
     * Returns the full location code in format: FLOOR-SECTION-SHELF[-POSITION]
     */
    public String getFullLocation() {
        StringBuilder sb = new StringBuilder();
        sb.append(floor).append("-").append(section).append("-").append(shelf);
        if (position != null) {
            sb.append("-").append(position);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ShelfLocation that = (ShelfLocation) o;
        return Objects.equals(floor, that.floor) &&
                Objects.equals(section, that.section) &&
                Objects.equals(shelf, that.shelf) &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, section, shelf, position);
    }

    @Override
    public String toString() {
        return getFullLocation();
    }
}
