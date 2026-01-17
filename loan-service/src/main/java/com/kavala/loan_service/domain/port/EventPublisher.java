package com.kavala.loan_service.domain.port;

import com.kavala.loan_service.domain.event.DomainEvent;

import java.util.List;

/**
 * Port for publishing domain events.
 * Abstracts the event publishing mechanism from the domain.
 */
public interface EventPublisher {

    /**
     * Publishes a single domain event.
     *
     * @param event the event to publish
     */
    void publish(DomainEvent event);

    /**
     * Publishes multiple domain events.
     *
     * @param events the events to publish
     */
    default void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
