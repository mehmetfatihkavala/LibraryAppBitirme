package com.kavala.catalog_service.infrastructure.adapter.persistence.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaBookEntity;

/**
 * Book için Spring Data JPA repository.
 * Temel CRUD işlemlerini JpaRepository'den miras alır.
 * Ayrıca arama ve filtreleme için özel query'ler içerir.
 */
@Repository
public interface SpringDataBookJpaRepository extends JpaRepository<JpaBookEntity, UUID> {

    /**
     * Kitapları başlık veya açıklamada anahtar kelimeye göre arar.
     */
    @Query("SELECT b FROM JpaBookEntity b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<JpaBookEntity> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Belirli bir dildeki kitapları getirir.
     */
    List<JpaBookEntity> findByLanguage(String language);

    /**
     * Belirli bir kategorideki kitapları getirir.
     */
    List<JpaBookEntity> findByCategoryId(UUID categoryId);

    /**
     * Belirli bir yayıncıya ait kitapları getirir.
     */
    List<JpaBookEntity> findByPublisherId(UUID publisherId);

    /**
     * Sayfa sayısı aralığına göre kitapları getirir.
     */
    List<JpaBookEntity> findByPageCountBetween(Integer minPageCount, Integer maxPageCount);
}
