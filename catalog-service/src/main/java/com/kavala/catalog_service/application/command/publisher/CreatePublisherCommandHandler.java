package com.kavala.catalog_service.application.command.publisher;

import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.CommandHandler;
import com.kavala.catalog_service.domain.publisher.Publisher;
import com.kavala.catalog_service.domain.publisher.PublisherId;
import com.kavala.catalog_service.domain.port.PublisherRepository;

public class CreatePublisherCommandHandler implements CommandHandler<CreatePublisherCommand, UUID> {

    private final PublisherRepository publisherRepository;

    public CreatePublisherCommandHandler(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public UUID handle(CreatePublisherCommand command) {
        PublisherId publisherId = PublisherId.generate();
        Publisher publisher = Publisher.createPublisher(publisherId, command.name());
        publisherRepository.save(publisher);
        return publisherId.value();
    }

}
