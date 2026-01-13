package com.kavala.catalog_service.infrastructure.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.domain.category.Category;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaCategoryEntity;

/**
 * Category domain modeli ile JPA entity arasında dönüşüm yapar.
 * Domain model veritabanı detaylarından bağımsız kalır.
 */
@Component
public class CategoryMapper {

    /**
     * Domain Category modelini JPA entity'ye dönüştürür.
     * 
     * @param category Domain Category nesnesi
     * @return JpaCategoryEntity veritabanı entity'si
     */
    public JpaCategoryEntity toEntity(Category category) {
        return new JpaCategoryEntity(
                category.getCategoryId().value(),
                category.getName(),
                category.getCreatedAt());
    }

    /**
     * JPA entity'yi domain Category modeline dönüştürür.
     * Domain model'in rehydrate metodunu kullanır.
     * 
     * @param entity JPA entity
     * @return Category domain nesnesi
     */
    public Category toDomain(JpaCategoryEntity entity) {
        return Category.rehydrate(
                CategoryId.of(entity.getId()),
                entity.getName(),
                entity.getCreatedAt());
    }
}
