package com.kavala.catalog_service.domain.port;

import java.util.List;
import java.util.Optional;

import com.kavala.catalog_service.domain.publisher.Publisher;
import com.kavala.catalog_service.domain.publisher.PublisherId;

public interface PublisherRepository {
    Publisher save(Publisher publisher);

    Publisher update(Publisher publisher);

    Optional<Publisher> findById(PublisherId publisherId);

    List<Publisher> findAll();

    void delete(Publisher publisher);

    boolean existsById(PublisherId publisherId);

}
