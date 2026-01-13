package com.kavala.catalog_service.infrastructure.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.book.model.Book;
import com.kavala.catalog_service.domain.book.model.BookId;
import com.kavala.catalog_service.domain.port.BookRepository;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaAuthorEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaBookEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaCategoryEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaPublisherEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.mapper.BookMapper;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataAuthorJpaRepository;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataBookJpaRepository;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataCategoryJpaRepository;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataPublisherJpaRepository;

/**
 * BookRepository port'unun implementasyonu.
 * Domain katmanını JPA/veritabanı detaylarından izole eder.
 * Hexagonal Architecture'da "driven adapter" rolünü üstlenir.
 * 
 * Book-Author M2M ilişkisini yönetir.
 */
@Repository
public class BookRepositoryAdapter implements BookRepository {

    private final SpringDataBookJpaRepository bookJpaRepository;
    private final SpringDataCategoryJpaRepository categoryJpaRepository;
    private final SpringDataPublisherJpaRepository publisherJpaRepository;
    private final SpringDataAuthorJpaRepository authorJpaRepository;
    private final BookMapper mapper;

    public BookRepositoryAdapter(
            SpringDataBookJpaRepository bookJpaRepository,
            SpringDataCategoryJpaRepository categoryJpaRepository,
            SpringDataPublisherJpaRepository publisherJpaRepository,
            SpringDataAuthorJpaRepository authorJpaRepository,
            BookMapper mapper) {
        this.bookJpaRepository = bookJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.publisherJpaRepository = publisherJpaRepository;
        this.authorJpaRepository = authorJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book) {
        // Category ve Publisher entity'lerini çek
        JpaCategoryEntity category = categoryJpaRepository
                .findById(book.getCategoryId().value())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category not found: " + book.getCategoryId().value()));

        JpaPublisherEntity publisher = publisherJpaRepository
                .findById(book.getPublisherId().value())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Publisher not found: " + book.getPublisherId().value()));

        // Book entity oluştur
        JpaBookEntity bookEntity = mapper.toEntity(book, category, publisher);

        // Author ilişkilerini ekle
        for (AuthorId authorId : book.getAuthorIds()) {
            JpaAuthorEntity author = authorJpaRepository
                    .findById(authorId.value())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Author not found: " + authorId.value()));
            bookEntity.addAuthor(author);
        }

        JpaBookEntity savedEntity = bookJpaRepository.save(bookEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Book update(Book book) {
        // Mevcut entity'yi bul
        JpaBookEntity existingEntity = bookJpaRepository
                .findById(book.getId().value())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Book not found: " + book.getId().value()));

        // Category ve Publisher entity'lerini çek
        JpaCategoryEntity category = categoryJpaRepository
                .findById(book.getCategoryId().value())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category not found: " + book.getCategoryId().value()));

        JpaPublisherEntity publisher = publisherJpaRepository
                .findById(book.getPublisherId().value())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Publisher not found: " + book.getPublisherId().value()));

        // Entity'yi güncelle
        mapper.updateEntity(existingEntity, book, category, publisher);

        // Author ilişkilerini güncelle
        existingEntity.clearAuthors();
        for (AuthorId authorId : book.getAuthorIds()) {
            JpaAuthorEntity author = authorJpaRepository
                    .findById(authorId.value())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Author not found: " + authorId.value()));
            existingEntity.addAuthor(author);
        }

        JpaBookEntity savedEntity = bookJpaRepository.save(existingEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Book> findById(BookId bookId) {
        return bookJpaRepository.findById(bookId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return bookJpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Book book) {
        bookJpaRepository.deleteById(book.getId().value());
    }

    @Override
    public boolean existsById(BookId bookId) {
        return bookJpaRepository.existsById(bookId.value());
    }
}
