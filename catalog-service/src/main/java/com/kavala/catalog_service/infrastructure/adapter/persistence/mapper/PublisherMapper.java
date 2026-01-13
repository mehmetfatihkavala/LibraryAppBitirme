package com.kavala.catalog_service.infrastructure.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.domain.publisher.Publisher;
import com.kavala.catalog_service.domain.publisher.PublisherId;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaPublisherEntity;

/**
 * Publisher domain modeli ile JPA entity arasında dönüşüm yapar.
 * Domain model veritabanı detaylarından bağımsız kalır.
 */
@Component
public class PublisherMapper {

    /**
     * Domain Publisher modelini JPA entity'ye dönüştürür.
     * 
     * @param publisher Domain Publisher nesnesi
     * @return JpaPublisherEntity veritabanı entity'si
     */
    public JpaPublisherEntity toEntity(Publisher publisher) {
        return new JpaPublisherEntity(
                publisher.getPublisherId().value(),
                publisher.getName(),
                publisher.getCreatedAt());
    }

    /**
     * JPA entity'yi domain Publisher modeline dönüştürür.
     * Domain model'in rehydrate metodunu kullanır.
     * 
     * @param entity JPA entity
     * @return Publisher domain nesnesi
     */
    public Publisher toDomain(JpaPublisherEntity entity) {
        return Publisher.rehydrate(
                PublisherId.of(entity.getId()),
                entity.getName(),
                entity.getCreatedAt());
    }
}
