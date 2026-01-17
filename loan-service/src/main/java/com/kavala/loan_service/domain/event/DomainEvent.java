package com.kavala.loan_service.domain.event;

import java.time.LocalDateTime;

/**
 * Marker interface for domain events.
 * All domain events in the loan-service should implement this interface.
 */
public interface DomainEvent {

    /**
     * Returns the timestamp when the event occurred.
     */
    LocalDateTime occurredAt();
}
