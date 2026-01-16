package com.kavala.inventory_service.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository for BookCopy entities.
 * Provides CRUD and custom query operations.
 */
@Repository
public interface SpringDataBookCopyJpaRepository extends JpaRepository<JpaBookCopyEntity, UUID> {

    Optional<JpaBookCopyEntity> findByBarcode(String barcode);

    List<JpaBookCopyEntity> findByBookId(UUID bookId);

    List<JpaBookCopyEntity> findByStatus(JpaBookCopyEntity.CopyStatusEntity status);

    List<JpaBookCopyEntity> findBySection(String section);

    boolean existsByBarcode(String barcode);

    long countByBookId(UUID bookId);

    @Query("SELECT e FROM JpaBookCopyEntity e WHERE e.bookId = :bookId AND e.status = 'AVAILABLE'")
    List<JpaBookCopyEntity> findAvailableCopiesByBookId(@Param("bookId") UUID bookId);

    @Query("SELECT COUNT(e) FROM JpaBookCopyEntity e WHERE e.bookId = :bookId AND e.status = 'AVAILABLE'")
    long countAvailableCopiesByBookId(@Param("bookId") UUID bookId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM JpaBookCopyEntity e " +
            "WHERE e.bookId = :bookId AND e.status = 'AVAILABLE'")
    boolean hasAvailableCopy(@Param("bookId") UUID bookId);

    @Query("SELECT e FROM JpaBookCopyEntity e WHERE e.floor IS NULL OR e.section IS NULL OR e.shelf IS NULL")
    List<JpaBookCopyEntity> findCopiesWithoutShelfLocation();

    @Query("SELECT e FROM JpaBookCopyEntity e ORDER BY e.acquiredAt DESC LIMIT :limit")
    List<JpaBookCopyEntity> findRecentlyAcquired(@Param("limit") int limit);

    @Query("SELECT e FROM JpaBookCopyEntity e WHERE e.barcode LIKE %:pattern%")
    List<JpaBookCopyEntity> searchByBarcodePattern(@Param("pattern") String pattern);

    @Query("SELECT e FROM JpaBookCopyEntity e WHERE e.floor = :floor AND e.section = :section AND e.shelf = :shelf")
    List<JpaBookCopyEntity> findByShelfLocation(
            @Param("floor") String floor,
            @Param("section") String section,
            @Param("shelf") String shelf);
}
