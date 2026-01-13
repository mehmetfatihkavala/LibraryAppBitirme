package com.kavala.catalog_service.infrastructure.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.kavala.catalog_service.domain.category.Category;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.domain.port.CategoryRepository;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaCategoryEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.mapper.CategoryMapper;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataCategoryJpaRepository;

/**
 * CategoryRepository port'unun implementasyonu.
 * Domain katmanını JPA/veritabanı detaylarından izole eder.
 * Hexagonal Architecture'da "driven adapter" rolünü üstlenir.
 */
@Repository
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final SpringDataCategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    public CategoryRepositoryAdapter(SpringDataCategoryJpaRepository jpaRepository, CategoryMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Category save(Category category) {
        JpaCategoryEntity entity = mapper.toEntity(category);
        JpaCategoryEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Category update(Category category) {
        JpaCategoryEntity entity = mapper.toEntity(category);
        JpaCategoryEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        return jpaRepository.findById(categoryId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Category category) {
        jpaRepository.deleteById(category.getCategoryId().value());
    }

    @Override
    public boolean existsById(CategoryId categoryId) {
        return jpaRepository.existsById(categoryId.value());
    }
}
