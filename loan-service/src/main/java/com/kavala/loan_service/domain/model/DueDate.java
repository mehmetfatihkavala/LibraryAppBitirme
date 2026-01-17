package com.kavala.loan_service.domain.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Value Object representing a loan due date.
 * Immutable and self-validating with overdue checking logic.
 */
public final class DueDate {

    private final LocalDate value;

    private DueDate(LocalDate value) {
        this.value = Objects.requireNonNull(value, "DueDate value cannot be null");
    }

    public static DueDate of(LocalDate value) {
        return new DueDate(value);
    }

    /**
     * Creates a due date from today plus the given number of days.
     */
    public static DueDate fromDaysFromNow(int days) {
        if (days < 1) {
            throw new IllegalArgumentException("Days must be at least 1");
        }
        return new DueDate(LocalDate.now().plusDays(days));
    }

    /**
     * Creates a due date based on loan period (typically 14 or 21 days).
     */
    public static DueDate withDefaultPeriod() {
        return fromDaysFromNow(14);
    }

    public LocalDate getValue() {
        return value;
    }

    /**
     * Checks if this due date is overdue (past the current date).
     */
    public boolean isOverdue() {
        return LocalDate.now().isAfter(value);
    }

    /**
     * Checks if this due date is overdue as of a specific date.
     */
    public boolean isOverdueAsOf(LocalDate date) {
        return date.isAfter(value);
    }

    /**
     * Returns the number of days until due date.
     * Negative value means overdue by that many days.
     */
    public long daysUntilDue() {
        return ChronoUnit.DAYS.between(LocalDate.now(), value);
    }

    /**
     * Returns the number of days overdue (0 if not overdue).
     */
    public long daysOverdue() {
        long days = ChronoUnit.DAYS.between(value, LocalDate.now());
        return Math.max(0, days);
    }

    /**
     * Extends the due date by the given number of days.
     */
    public DueDate extend(int days) {
        if (days < 1) {
            throw new IllegalArgumentException("Extension days must be at least 1");
        }
        return new DueDate(value.plusDays(days));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DueDate dueDate = (DueDate) o;
        return Objects.equals(value, dueDate.value);
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
