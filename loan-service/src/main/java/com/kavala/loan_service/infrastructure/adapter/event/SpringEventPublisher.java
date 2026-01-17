package com.kavala.loan_service.infrastructure.adapter.event;

import com.kavala.loan_service.domain.event.DomainEvent;
import com.kavala.loan_service.domain.port.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation of EventPublisher using Spring's ApplicationEventPublisher.
 */
@Component
public class SpringEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher,
                "ApplicationEventPublisher cannot be null");
    }

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
