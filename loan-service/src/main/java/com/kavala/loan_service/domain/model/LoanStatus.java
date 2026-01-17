package com.kavala.loan_service.domain.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum representing the status of a Loan.
 * Supports state machine transitions for domain logic.
 */
public enum LoanStatus {

    OPEN("Active loan, book is with member", true),
    RETURNED("Book has been returned", false),
    OVERDUE("Loan is past due date", true);

    private final String description;
    private final boolean active;

    LoanStatus(String description, boolean active) {
        this.description = description;
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns true if the loan is still active (book not returned).
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the set of statuses that this status can transition to.
     * Implements a simple state machine for loan lifecycle.
     */
    public Set<LoanStatus> getAllowedTransitions() {
        return switch (this) {
            case OPEN -> EnumSet.of(RETURNED, OVERDUE);
            case OVERDUE -> EnumSet.of(RETURNED);
            case RETURNED -> EnumSet.noneOf(LoanStatus.class); // Terminal state
        };
    }

    /**
     * Checks if transition to the target status is allowed.
     */
    public boolean canTransitionTo(LoanStatus target) {
        return getAllowedTransitions().contains(target);
    }

    /**
     * Validates and returns the target status if transition is allowed.
     * 
     * @throws IllegalStateException if transition is not allowed
     */
    public LoanStatus transitionTo(LoanStatus target) {
        if (!canTransitionTo(target)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", this.name(), target.name()));
        }
        return target;
    }
}
