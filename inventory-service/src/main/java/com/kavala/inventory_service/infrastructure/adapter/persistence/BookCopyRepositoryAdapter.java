package com.kavala.inventory_service.infrastructure.adapter.persistence;

import com.kavala.inventory_service.domain.model.*;
import com.kavala.inventory_service.domain.port.BookCopyQueryPort;
import com.kavala.inventory_service.domain.port.BookCopyRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Adapter implementing repository and query ports using JPA.
 * Follows Hexagonal Architecture as the infrastructure adapter.
 */
@Repository
@Transactional
public class BookCopyRepositoryAdapter implements BookCopyRepository, BookCopyQueryPort {

    private final SpringDataBookCopyJpaRepository jpaRepository;
    private final BookCopyMapper mapper;

    public BookCopyRepositoryAdapter(
            SpringDataBookCopyJpaRepository jpaRepository,
            BookCopyMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    // ===================== BookCopyRepository (Write) =====================

    @Override
    public BookCopy save(BookCopy bookCopy) {
        JpaBookCopyEntity entity = jpaRepository.findById(bookCopy.getId().getValue())
                .map(existingEntity -> {
                    mapper.updateEntity(existingEntity, bookCopy);
                    return existingEntity;
                })
                .orElseGet(() -> mapper.toEntity(bookCopy));

        JpaBookCopyEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<BookCopy> findById(BookCopyId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<BookCopy> findByBarcode(Barcode barcode) {
        return jpaRepository.findByBarcode(barcode.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<BookCopy> findByBookId(BookId bookId) {
        return jpaRepository.findByBookId(bookId.getValue()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<BookCopy> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(BookCopyId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public void delete(BookCopy bookCopy) {
        jpaRepository.deleteById(bookCopy.getId().getValue());
    }

    @Override
    public boolean existsById(BookCopyId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByBarcode(Barcode barcode) {
        return jpaRepository.existsByBarcode(barcode.getValue());
    }

    @Override
    public long countByBookId(BookId bookId) {
        return jpaRepository.countByBookId(bookId.getValue());
    }

    // ===================== BookCopyQueryPort (Read) =====================

    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findAvailableCopiesByBookId(BookId bookId) {
        return jpaRepository.findAvailableCopiesByBookId(bookId.getValue()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findByStatus(CopyStatus status) {
        JpaBookCopyEntity.CopyStatusEntity entityStatus = toEntityStatus(status);
        return jpaRepository.findByStatus(entityStatus).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findByShelfLocation(ShelfLocation shelfLocation) {
        return jpaRepository.findByShelfLocation(
                shelfLocation.getFloor(),
                shelfLocation.getSection(),
                shelfLocation.getShelf()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findBySection(String section) {
        return jpaRepository.findBySection(section).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countAvailableCopiesByBookId(BookId bookId) {
        return jpaRepository.countAvailableCopiesByBookId(bookId.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(CopyStatus status) {
        JpaBookCopyEntity.CopyStatusEntity entityStatus = toEntityStatus(status);
        return jpaRepository.findByStatus(entityStatus).size();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAvailableCopy(BookId bookId) {
        return jpaRepository.hasAvailableCopy(bookId.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findCopiesWithoutShelfLocation() {
        return jpaRepository.findCopiesWithoutShelfLocation().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findRecentlyAcquired(int limit) {
        return jpaRepository.findRecentlyAcquired(limit).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> searchByBarcodePattern(String barcodePattern) {
        return jpaRepository.searchByBarcodePattern(barcodePattern).stream()
                .map(mapper::toDomain)
                .toList();
    }

    private JpaBookCopyEntity.CopyStatusEntity toEntityStatus(CopyStatus domainStatus) {
        return switch (domainStatus) {
            case AVAILABLE -> JpaBookCopyEntity.CopyStatusEntity.AVAILABLE;
            case LOANED -> JpaBookCopyEntity.CopyStatusEntity.LOANED;
            case RESERVED -> JpaBookCopyEntity.CopyStatusEntity.RESERVED;
            case LOST -> JpaBookCopyEntity.CopyStatusEntity.LOST;
            case DAMAGED -> JpaBookCopyEntity.CopyStatusEntity.DAMAGED;
            case WITHDRAWN -> JpaBookCopyEntity.CopyStatusEntity.WITHDRAWN;
        };
    }
}
