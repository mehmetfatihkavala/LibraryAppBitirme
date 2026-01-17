package com.kavala.loan_service.infrastructure.adapter.persistence;

import com.kavala.loan_service.domain.model.*;
import com.kavala.loan_service.domain.port.LoanRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of LoanRepository using Spring Data JPA.
 */
@Component
public class LoanRepositoryAdapter implements LoanRepository {

    private final SpringDataLoanJpaRepository jpaRepository;

    public LoanRepositoryAdapter(SpringDataLoanJpaRepository jpaRepository) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository, "JpaRepository cannot be null");
    }

    @Override
    public Loan save(Loan loan) {
        JpaLoanEntity entity = LoanMapper.toEntity(loan);
        JpaLoanEntity savedEntity = jpaRepository.save(entity);
        return LoanMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Loan> findById(LoanId id) {
        return jpaRepository.findById(id.getValue())
                .map(LoanMapper::toDomain);
    }

    @Override
    public List<Loan> findByMemberId(MemberId memberId) {
        return jpaRepository.findByMemberId(memberId.getValue())
                .stream()
                .map(LoanMapper::toDomain)
                .toList();
    }

    @Override
    public List<Loan> findByBookCopyId(BookCopyId bookCopyId) {
        return jpaRepository.findByBookCopyId(bookCopyId.getValue())
                .stream()
                .map(LoanMapper::toDomain)
                .toList();
    }

    @Override
    public List<Loan> findByStatus(LoanStatus status) {
        JpaLoanEntity.LoanStatusEntity statusEntity = switch (status) {
            case OPEN -> JpaLoanEntity.LoanStatusEntity.OPEN;
            case RETURNED -> JpaLoanEntity.LoanStatusEntity.RETURNED;
            case OVERDUE -> JpaLoanEntity.LoanStatusEntity.OVERDUE;
        };
        return jpaRepository.findByStatus(statusEntity)
                .stream()
                .map(LoanMapper::toDomain)
                .toList();
    }

    @Override
    public List<Loan> findAllOpen() {
        return jpaRepository.findAllOpen()
                .stream()
                .map(LoanMapper::toDomain)
                .toList();
    }

    @Override
    public List<Loan> findAllOverdue() {
        return jpaRepository.findAllOverdue()
                .stream()
                .map(LoanMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Loan> findActiveByBookCopyId(BookCopyId bookCopyId) {
        return jpaRepository.findActiveByBookCopyId(bookCopyId.getValue())
                .map(LoanMapper::toDomain);
    }

    @Override
    public long countActiveByMemberId(MemberId memberId) {
        return jpaRepository.countActiveByMemberId(memberId.getValue());
    }

    @Override
    public List<Loan> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(LoanMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(LoanId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public void deleteById(LoanId id) {
        jpaRepository.deleteById(id.getValue());
    }
}
