package com.kavala.catalog_service.domain.book.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Book {
    private final BookId id;
    private final String title;
    private final LocalDate publishedDate;
    private final Integer pageCount;
    private final String language;
    private final String description;
    private final Instant createdAt;

    private Book(BookId id, String title, LocalDate publishedDate, Integer pageCount, String language,
            String description, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.language = language;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static Book createNewBook(String title, LocalDate publishedDate, Integer pageCount, String language,
            String description) {
        validateTitle(title);
        validatePublishedDate(publishedDate);
        validatePageCount(pageCount);
        validateLanguage(language);
        validateDescription(description);
        return new Book(BookId.generate(), title, publishedDate, pageCount, language, description, Instant.now());
    }

    public static Book rehydrate(BookId id, String title, LocalDate publishedDate, Integer pageCount, String language,
            String description, Instant createdAt) {
        Book book = new Book(id, title, publishedDate, pageCount, language, description, createdAt);
        return book;
    }

    public static Book updateBook(Book book, String title, LocalDate publishedDate, Integer pageCount, String language,
            String description) {
        validateTitle(title);
        validatePublishedDate(publishedDate);
        validatePageCount(pageCount);
        validateLanguage(language);
        validateDescription(description);
        return new Book(book.getId(), title, publishedDate, pageCount, language, description, book.getCreatedAt());
    }

    private static void validateTitle(String title) {
        Objects.requireNonNull(title, "Title cannot be null");
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Title cannot be longer than 255 characters");
        }
    }

    private static void validatePublishedDate(LocalDate publishedDate) {
        Objects.requireNonNull(publishedDate, "PublishedDate cannot be null");
        if (publishedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("PublishedDate cannot be in the future");
        }
    }

    private static void validatePageCount(Integer pageCount) {
        Objects.requireNonNull(pageCount, "PageCount cannot be null");
        if (pageCount < 0) {
            throw new IllegalArgumentException("PageCount cannot be negative");
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        return Objects.equals(id, other.id);
    }

    public BookId getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

}
