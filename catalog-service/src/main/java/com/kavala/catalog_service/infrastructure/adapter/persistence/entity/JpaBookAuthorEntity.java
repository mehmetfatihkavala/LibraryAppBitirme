package com.kavala.catalog_service.infrastructure.adapter.persistence.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * Book ve Author arasındaki Many-to-Many ilişkisini yöneten join entity.
 * Composite primary key kullanır (bookId + authorId).
 */
@Entity
@Table(name = "book_authors")
public class JpaBookAuthorEntity {

    @EmbeddedId
    private BookAuthorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    private JpaBookEntity book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorId")
    @JoinColumn(name = "author_id", nullable = false)
    private JpaAuthorEntity author;

    protected JpaBookAuthorEntity() {
        // JPA için gerekli
    }

    public JpaBookAuthorEntity(JpaBookEntity book, JpaAuthorEntity author) {
        this.book = book;
        this.author = author;
        this.id = new BookAuthorId(book.getId(), author.getId());
    }

    public BookAuthorId getId() {
        return id;
    }

    public JpaBookEntity getBook() {
        return book;
    }

    public void setBook(JpaBookEntity book) {
        this.book = book;
    }

    public JpaAuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(JpaAuthorEntity author) {
        this.author = author;
    }

    /**
     * Composite Primary Key sınıfı.
     */
    @Embeddable
    public static class BookAuthorId implements Serializable {

        @Column(name = "book_id")
        private UUID bookId;

        @Column(name = "author_id")
        private UUID authorId;

        protected BookAuthorId() {
            // JPA için gerekli
        }

        public BookAuthorId(UUID bookId, UUID authorId) {
            this.bookId = bookId;
            this.authorId = authorId;
        }

        public UUID getBookId() {
            return bookId;
        }

        public UUID getAuthorId() {
            return authorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            BookAuthorId that = (BookAuthorId) o;
            return Objects.equals(bookId, that.bookId) && Objects.equals(authorId, that.authorId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bookId, authorId);
        }
    }
}
