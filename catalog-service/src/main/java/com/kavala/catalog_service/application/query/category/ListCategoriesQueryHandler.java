package com.kavala.catalog_service.application.query.category;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kavala.catalog_service.api.rest.dto.CategoryResponse;
import com.kavala.catalog_service.core.cqrs.QueryHandler;
import com.kavala.catalog_service.domain.category.Category;
import com.kavala.catalog_service.domain.port.CategoryRepository;

@Component
public class ListCategoriesQueryHandler implements QueryHandler<ListCategoriesQuery, List<CategoryResponse>> {

    private final CategoryRepository categoryRepository;

    public ListCategoriesQueryHandler(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> handle(ListCategoriesQuery query) {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryResponse(category.getCategoryId().value(), category.getName()))
                .toList();
    }

}
