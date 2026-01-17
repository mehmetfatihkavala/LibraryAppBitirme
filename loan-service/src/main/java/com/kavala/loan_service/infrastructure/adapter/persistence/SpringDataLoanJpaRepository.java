package com.kavala.loan_service.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for JpaLoanEntity.
 */
@Repository
public interface SpringDataLoanJpaRepository extends JpaRepository<JpaLoanEntity, UUID> {

    List<JpaLoanEntity> findByMemberId(UUID memberId);

    List<JpaLoanEntity> findByBookCopyId(UUID bookCopyId);

    List<JpaLoanEntity> findByStatus(JpaLoanEntity.LoanStatusEntity status);

    @Query("SELECT l FROM JpaLoanEntity l WHERE l.status IN ('OPEN', 'OVERDUE')")
    List<JpaLoanEntity> findAllOpen();

    @Query("SELECT l FROM JpaLoanEntity l WHERE l.status = 'OVERDUE'")
    List<JpaLoanEntity> findAllOverdue();

    @Query("SELECT l FROM JpaLoanEntity l WHERE l.bookCopyId = :bookCopyId AND l.status IN ('OPEN', 'OVERDUE')")
    Optional<JpaLoanEntity> findActiveByBookCopyId(@Param("bookCopyId") UUID bookCopyId);

    @Query("SELECT COUNT(l) FROM JpaLoanEntity l WHERE l.memberId = :memberId AND l.status IN ('OPEN', 'OVERDUE')")
    long countActiveByMemberId(@Param("memberId") UUID memberId);
}
