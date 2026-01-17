package com.kavala.loan_service.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing a fine amount.
 * Immutable, supports currency and calculations.
 */
public final class FineAmount {

    public static final String DEFAULT_CURRENCY = "TRY";
    public static final BigDecimal DAILY_FINE_RATE = new BigDecimal("5.00"); // 5 TRY per day

    private final BigDecimal amount;
    private final String currency;

    private FineAmount(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fine amount cannot be null or negative");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }

    public static FineAmount of(BigDecimal amount) {
        return new FineAmount(amount, DEFAULT_CURRENCY);
    }

    public static FineAmount of(BigDecimal amount, String currency) {
        return new FineAmount(amount, currency);
    }

    public static FineAmount zero() {
        return new FineAmount(BigDecimal.ZERO, DEFAULT_CURRENCY);
    }

    /**
     * Calculates fine based on number of days overdue.
     */
    public static FineAmount calculateFromDaysOverdue(long daysOverdue) {
        if (daysOverdue <= 0) {
            return zero();
        }
        BigDecimal amount = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(daysOverdue));
        return new FineAmount(amount, DEFAULT_CURRENCY);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Adds another fine amount (must be same currency).
     */
    public FineAmount add(FineAmount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add fines with different currencies");
        }
        return new FineAmount(this.amount.add(other.amount), this.currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FineAmount that = (FineAmount) o;
        return Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%s %s", amount, currency);
    }
}
