package com.kavala.catalog_service.domain.category;

import java.time.Instant;
import java.util.Objects;

public class Category {
    private final CategoryId categoryId;
    private final String name;
    private final Instant createdAt;

    private Category(CategoryId categoryId, String name, Instant createdAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static Category createCategory(CategoryId categoryId, String name) {
        validateName(name);
        return new Category(categoryId, name, Instant.now());
    }

    public static Category rehydrate(CategoryId categoryId, String name, Instant createdAt) {
        return new Category(categoryId, name, createdAt);
    }

    public static Category updateCategory(Category category, String name) {
        validateName(name);
        return new Category(category.getCategoryId(), name, category.getCreatedAt());
    }

    private static void validateName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Name cannot be longer than 255 characters");
        }
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category other = (Category) obj;
        return Objects.equals(categoryId, other.categoryId);
    }
}
