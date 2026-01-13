package com.kavala.catalog_service.application.command.category;

import java.util.UUID;

import com.kavala.catalog_service.core.cqrs.CommandHandler;
import com.kavala.catalog_service.domain.category.Category;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.domain.port.CategoryRepository;

public class CreateCategoryCommandHandler implements CommandHandler<CreateCategoryCommand, UUID> {

    private final CategoryRepository categoryRepository;

    public CreateCategoryCommandHandler(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public UUID handle(CreateCategoryCommand command) {
        CategoryId categoryId = CategoryId.generate();
        Category category = Category.createCategory(categoryId, command.name());
        categoryRepository.save(category);
        return categoryId.value();
    }

}
