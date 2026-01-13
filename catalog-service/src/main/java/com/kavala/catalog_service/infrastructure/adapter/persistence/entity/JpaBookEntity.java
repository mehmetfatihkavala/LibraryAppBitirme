package com.kavala.catalog_service.infrastructure.adapter.persistence.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Book domain entity'sinin JPA karşılığı.
 * Veritabanı ile domain model arasında köprü görevi görür.
 * Author'larla Many-to-Many ilişkisi JpaBookAuthorEntity üzerinden yönetilir.
 */
@Entity
@Table(name = "books")
public class JpaBookEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private JpaCategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private JpaPublisherEntity publisher;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @Column(name = "page_count", nullable = false)
    private Integer pageCount;

    @Column(name = "language", nullable = false, length = 50)
    private String language;

    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<JpaBookAuthorEntity> bookAuthors = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected JpaBookEntity() {
        // JPA için gerekli
    }

    public JpaBookEntity(UUID id, String title, JpaCategoryEntity category, JpaPublisherEntity publisher,
            LocalDate publishedDate, Integer pageCount, String language, String description,
            Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.language = language;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Helper methods for M2M relationship
    public void addAuthor(JpaAuthorEntity author) {
        JpaBookAuthorEntity bookAuthor = new JpaBookAuthorEntity(this, author);
        bookAuthors.add(bookAuthor);
    }

    public void removeAuthor(JpaAuthorEntity author) {
        bookAuthors.removeIf(ba -> ba.getAuthor().getId().equals(author.getId()));
    }

    public void clearAuthors() {
        bookAuthors.clear();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JpaCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(JpaCategoryEntity category) {
        this.category = category;
    }

    public JpaPublisherEntity getPublisher() {
        return publisher;
    }

    public void setPublisher(JpaPublisherEntity publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JpaBookAuthorEntity> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<JpaBookAuthorEntity> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
