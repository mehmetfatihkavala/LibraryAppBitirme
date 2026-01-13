package com.kavala.catalog_service.infrastructure.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.domain.author.Author;
import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaAuthorEntity;

/**
 * Author domain modeli ile JPA entity arasında dönüşüm yapar.
 * Domain model veritabanı detaylarından bağımsız kalır.
 */
@Component
public class AuthorMapper {

    /**
     * Domain Author modelini JPA entity'ye dönüştürür.
     * 
     * @param author Domain Author nesnesi
     * @return JpaAuthorEntity veritabanı entity'si
     */
    public JpaAuthorEntity toEntity(Author author) {
        return new JpaAuthorEntity(
                author.getAuthorId().value(),
                author.getFullName(),
                author.getBirthDate(),
                author.getNationality(),
                author.getCreatedAt());
    }

    /**
     * JPA entity'yi domain Author modeline dönüştürür.
     * Domain model'in rehydrate metodunu kullanır.
     * 
     * @param entity JPA entity
     * @return Author domain nesnesi
     */
    public Author toDomain(JpaAuthorEntity entity) {
        return Author.rehydrate(
                AuthorId.of(entity.getId()),
                entity.getFullName(),
                entity.getBirthDate(),
                entity.getNationality(),
                entity.getCreatedAt());
    }
}
