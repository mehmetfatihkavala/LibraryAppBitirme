package com.kavala.catalog_service.infrastructure.adapter.persistence.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaPublisherEntity;

/**
 * Publisher için Spring Data JPA repository.
 * Temel CRUD işlemlerini JpaRepository'den miras alır.
 */
@Repository
public interface SpringDataPublisherJpaRepository extends JpaRepository<JpaPublisherEntity, UUID> {

}
