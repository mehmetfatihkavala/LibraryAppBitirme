package com.kavala.catalog_service.infrastructure.adapter.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.domain.book.model.BookId;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.domain.publisher.PublisherId;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaBookAuthorEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaBookEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaCategoryEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaPublisherEntity;

/**
 * Book domain modeli ile JPA entity arasında dönüşüm yapar.
 * M2M Author ilişkisini de yönetir.
 * Domain model veritabanı detaylarından bağımsız kalır.
 */
@Component
public class BookMapper {

    /**
     * Domain Book modelini JPA entity'ye dönüştürür.
     * NOT: Category ve Publisher entity'leri dışarıdan sağlanmalıdır.
     * Author ilişkileri adapter tarafından ayrıca yönetilir.
     * 
     * @param book      Domain Book nesnesi
     * @param category  JPA Category entity
     * @param publisher JPA Publisher entity
     * @return JpaBookEntity veritabanı entity'si
     */
    public JpaBookEntity toEntity(Book book, JpaCategoryEntity category, JpaPublisherEntity publisher) {
        return new JpaBookEntity(
                book.getId().value(),
                book.getTitle(),
                category,
                publisher,
                book.getPublishedDate(),
                book.getPageCount(),
                book.getLanguage(),
                book.getDescription(),
                book.getCreatedAt(),
                book.getUpdatedAt());
    }

    /**
     * Mevcut JPA entity'yi domain Book modelindeki değerlerle günceller.
     * 
     * @param entity    Güncellenecek JPA entity
     * @param book      Kaynak domain Book nesnesi
     * @param category  Yeni JPA Category entity
     * @param publisher Yeni JPA Publisher entity
     */
    public void updateEntity(JpaBookEntity entity, Book book,
            JpaCategoryEntity category, JpaPublisherEntity publisher) {
        entity.setTitle(book.getTitle());
        entity.setCategory(category);
        entity.setPublisher(publisher);
        entity.setPublishedDate(book.getPublishedDate());
        entity.setPageCount(book.getPageCount());
        entity.setLanguage(book.getLanguage());
        entity.setDescription(book.getDescription());
        entity.setUpdatedAt(book.getUpdatedAt());
    }

    /**
     * JPA entity'yi domain Book modeline dönüştürür.
     * M2M Author ilişkisinden AuthorId listesi çıkarılır.
     * Domain model'in rehydrate metodunu kullanır.
     * 
     * @param entity JPA entity
     * @return Book domain nesnesi
     */
    public Book toDomain(JpaBookEntity entity) {
        List<AuthorId> authorIds = entity.getBookAuthors().stream()
                .map(JpaBookAuthorEntity::getAuthor)
                .map(author -> AuthorId.of(author.getId()))
                .collect(Collectors.toList());

        return Book.rehydrate(
                BookId.of(entity.getId()),
                entity.getTitle(),
                CategoryId.of(entity.getCategory().getId()),
                PublisherId.of(entity.getPublisher().getId()),
                entity.getPublishedDate(),
                entity.getPageCount(),
                entity.getLanguage(),
                entity.getDescription(),
                authorIds,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
