package com.kavala.catalog_service.infrastructure.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.kavala.catalog_service.domain.port.PublisherRepository;
import com.kavala.catalog_service.domain.publisher.Publisher;
import com.kavala.catalog_service.domain.publisher.PublisherId;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaPublisherEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.mapper.PublisherMapper;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataPublisherJpaRepository;

/**
 * PublisherRepository port'unun implementasyonu.
 * Domain katmanını JPA/veritabanı detaylarından izole eder.
 * Hexagonal Architecture'da "driven adapter" rolünü üstlenir.
 */
@Repository
public class PublisherRepositoryAdapter implements PublisherRepository {

    private final SpringDataPublisherJpaRepository jpaRepository;
    private final PublisherMapper mapper;

    public PublisherRepositoryAdapter(SpringDataPublisherJpaRepository jpaRepository, PublisherMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Publisher save(Publisher publisher) {
        JpaPublisherEntity entity = mapper.toEntity(publisher);
        JpaPublisherEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Publisher update(Publisher publisher) {
        JpaPublisherEntity entity = mapper.toEntity(publisher);
        JpaPublisherEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Publisher> findById(PublisherId publisherId) {
        return jpaRepository.findById(publisherId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Publisher> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Publisher publisher) {
        jpaRepository.deleteById(publisher.getPublisherId().value());
    }

    @Override
    public boolean existsById(PublisherId publisherId) {
        return jpaRepository.existsById(publisherId.value());
    }
}
