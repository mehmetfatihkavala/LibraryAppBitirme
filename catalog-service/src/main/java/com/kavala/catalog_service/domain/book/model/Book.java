package com.kavala.catalog_service.domain.book.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.category.CategoryId;
import com.kavala.catalog_service.domain.publisher.PublisherId;

public class Book {
    private final BookId id;
    private final String title;
    private final CategoryId categoryId;
    private final PublisherId publisherId;
    private final LocalDate publishedDate;
    private final Integer pageCount;
    private final String language;
    private final String description;
    private final List<AuthorId> authorIds;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Book(BookId id, String title, CategoryId categoryId, PublisherId publisherId,
            LocalDate publishedDate, Integer pageCount, String language, String description,
            List<AuthorId> authorIds, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.publisherId = publisherId;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.language = language;
        this.description = description;
        this.authorIds = new ArrayList<>(authorIds);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Book createNewBook(String title, CategoryId categoryId, PublisherId publisherId,
            LocalDate publishedDate, Integer pageCount, String language,
            String description, List<AuthorId> authorIds) {
        validateTitle(title);
        validateCategoryId(categoryId);
        validatePublisherId(publisherId);
        validatePublishedDate(publishedDate);
        validatePageCount(pageCount);
        validateLanguage(language);
        validateDescription(description);
        validateAuthorIds(authorIds);

        Instant now = Instant.now();
        return new Book(BookId.generate(), title, categoryId, publisherId, publishedDate,
                pageCount, language, description, authorIds, now, now);
    }

    public static Book rehydrate(BookId id, String title, CategoryId categoryId,
            PublisherId publisherId, LocalDate publishedDate,
            Integer pageCount, String language, String description,
            List<AuthorId> authorIds, Instant createdAt, Instant updatedAt) {
        return new Book(id, title, categoryId, publisherId, publishedDate, pageCount,
                language, description, authorIds, createdAt, updatedAt);
    }

    public Book updateTitle(String newTitle) {
        validateTitle(newTitle);
        return new Book(this.id, newTitle, this.categoryId, this.publisherId,
                this.publishedDate, this.pageCount, this.language, this.description,
                this.authorIds, this.createdAt, Instant.now());
    }

    public Book updatePublishedDate(LocalDate newPublishedDate) {
        validatePublishedDate(newPublishedDate);
        return new Book(this.id, this.title, this.categoryId, this.publisherId,
                newPublishedDate, this.pageCount, this.language, this.description,
                this.authorIds, this.createdAt, Instant.now());
    }

    public Book updatePageCount(Integer newPageCount) {
        validatePageCount(newPageCount);
        return new Book(this.id, this.title, this.categoryId, this.publisherId,
                this.publishedDate, newPageCount, this.language, this.description,
                this.authorIds, this.createdAt, Instant.now());
    }

    public Book changePublisher(PublisherId newPublisherId) {
        validatePublisherId(newPublisherId);
        return new Book(this.id, this.title, this.categoryId, newPublisherId,
                this.publishedDate, this.pageCount, this.language, this.description,
                this.authorIds, this.createdAt, Instant.now());
    }

    public Book changeCategory(CategoryId newCategoryId) {
        validateCategoryId(newCategoryId);
        return new Book(this.id, this.title, newCategoryId, this.publisherId,
                this.publishedDate, this.pageCount, this.language, this.description,
                this.authorIds, this.createdAt, Instant.now());
    }

    public Book updateLanguage(String newLanguage) {
        validateLanguage(newLanguage);
        return new Book(this.id, this.title, this.categoryId, this.publisherId,
                this.publishedDate, this.pageCount, newLanguage, this.description,
                this.authorIds, this.createdAt, Instant.now());
    }

    public Book updateDescription(String newDescription) {
        validateDescription(newDescription);
        return new Book(this.id, this.title, this.categoryId, this.publisherId,
                this.publishedDate, this.pageCount, this.language, newDescription,
                this.authorIds, this.createdAt, Instant.now());
    }

    public Book addAuthor(AuthorId authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("AuthorId cannot be null");
        }
        if (this.authorIds.contains(authorId)) {
            throw new IllegalArgumentException("Author already exists");
        }

        List<AuthorId> newAuthorIds = new ArrayList<>(this.authorIds);
        newAuthorIds.add(authorId);

        return new Book(this.id, this.title, this.categoryId, this.publisherId,
                this.publishedDate, this.pageCount, this.language, this.description,
                newAuthorIds, this.createdAt, Instant.now());
    }

    public Book removeAuthor(AuthorId authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("AuthorId cannot be null");
        }
        if (!this.authorIds.contains(authorId)) {
            throw new IllegalArgumentException("Author does not exist");
        }

        List<AuthorId> newAuthorIds = new ArrayList<>(this.authorIds);
        newAuthorIds.remove(authorId);

        return new Book(this.id, this.title, this.categoryId, this.publisherId,
                this.publishedDate, this.pageCount, this.language, this.description,
                newAuthorIds, this.createdAt, Instant.now());
    }

    // Validations
    private static void validateTitle(String title) {
        Objects.requireNonNull(title, "Title cannot be null");
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Title cannot be longer than 255 characters");
        }
    }

    private static void validateCategoryId(CategoryId categoryId) {
        Objects.requireNonNull(categoryId, "CategoryId cannot be null");
    }

    private static void validatePublisherId(PublisherId publisherId) {
        Objects.requireNonNull(publisherId, "PublisherId cannot be null");
    }

    private static void validatePublishedDate(LocalDate publishedDate) {
        Objects.requireNonNull(publishedDate, "PublishedDate cannot be null");
        if (publishedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("PublishedDate cannot be in the future");
        }
    }

    private static void validatePageCount(Integer pageCount) {
        Objects.requireNonNull(pageCount, "PageCount cannot be null");
        if (pageCount < 1) {
            throw new IllegalArgumentException("PageCount must be positive");
        }
        if (pageCount > 50000) {
            throw new IllegalArgumentException("PageCount seems unrealistic");
        }
    }

    private static void validateLanguage(String language) {
        Objects.requireNonNull(language, "Language cannot be null");
        if (language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be empty");
        }
    }

    private static void validateDescription(String description) {
        Objects.requireNonNull(description, "Description cannot be null");
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (description.length() > 500) {
            throw new IllegalArgumentException("Description cannot be longer than 500 characters");
        }
    }

    private static void validateAuthorIds(List<AuthorId> authorIds) {
        Objects.requireNonNull(authorIds, "AuthorIds cannot be null");
        if (authorIds.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one author");
        }
    }

    // Getters
    public BookId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public PublisherId getPublisherId() {
        return publisherId;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public List<AuthorId> getAuthorIds() {
        return Collections.unmodifiableList(authorIds);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        return Objects.equals(id, other.id);
    }
}