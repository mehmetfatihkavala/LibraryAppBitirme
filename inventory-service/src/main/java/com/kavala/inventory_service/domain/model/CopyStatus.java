package com.kavala.inventory_service.domain.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum representing the status of a BookCopy.
 * Supports state machine transitions for domain logic.
 */
public enum CopyStatus {

    AVAILABLE("Available for loan", true),
    LOANED("Currently on loan", false),
    RESERVED("Reserved by a member", false),
    LOST("Reported as lost", false),
    DAMAGED("Damaged, needs repair", false),
    WITHDRAWN("Withdrawn from circulation", false);

    private final String description;
    private final boolean availableForLoan;

    CopyStatus(String description, boolean availableForLoan) {
        this.description = description;
        this.availableForLoan = availableForLoan;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailableForLoan() {
        return availableForLoan;
    }

    /**
     * Returns the set of statuses that this status can transition to.
     * Implements a simple state machine for copy lifecycle.
     */
    public Set<CopyStatus> getAllowedTransitions() {
        return switch (this) {
            case AVAILABLE -> EnumSet.of(LOANED, RESERVED, LOST, DAMAGED, WITHDRAWN);
            case LOANED -> EnumSet.of(AVAILABLE, LOST, DAMAGED);
            case RESERVED -> EnumSet.of(AVAILABLE, LOANED, LOST);
            case LOST -> EnumSet.of(AVAILABLE, WITHDRAWN);
            case DAMAGED -> EnumSet.of(AVAILABLE, WITHDRAWN);
            case WITHDRAWN -> EnumSet.noneOf(CopyStatus.class); // Terminal state
        };
    }

    /**
     * Checks if transition to the target status is allowed.
     */
    public boolean canTransitionTo(CopyStatus target) {
        return getAllowedTransitions().contains(target);
    }

    /**
     * Validates and returns the target status if transition is allowed.
     * 
     * @throws IllegalStateException if transition is not allowed
     */
    public CopyStatus transitionTo(CopyStatus target) {
        if (!canTransitionTo(target)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", this.name(), target.name()));
        }
        return target;
    }
}
