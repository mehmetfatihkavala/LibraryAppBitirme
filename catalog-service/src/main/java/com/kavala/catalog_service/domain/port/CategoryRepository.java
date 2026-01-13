package com.kavala.catalog_service.domain.port;

import java.util.List;
import java.util.Optional;

import com.kavala.catalog_service.domain.category.Category;
import com.kavala.catalog_service.domain.category.CategoryId;

public interface CategoryRepository {
    Category save(Category category);

    Category update(Category category);

    Optional<Category> findById(CategoryId categoryId);

    List<Category> findAll();

    void delete(Category category);

    boolean existsById(CategoryId categoryId);

}
