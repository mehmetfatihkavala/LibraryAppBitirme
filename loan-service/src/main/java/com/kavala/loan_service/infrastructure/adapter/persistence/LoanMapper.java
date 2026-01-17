package com.kavala.loan_service.infrastructure.adapter.persistence;

import com.kavala.loan_service.domain.model.*;

import java.math.BigDecimal;

/**
 * Mapper between domain Loan and JPA entity.
 */
public class LoanMapper {

    private LoanMapper() {
        // Utility class
    }

    /**
     * Maps domain Loan to JPA entity.
     */
    public static JpaLoanEntity toEntity(Loan loan) {
        JpaLoanEntity entity = new JpaLoanEntity();
        entity.setId(loan.getId().getValue());
        entity.setMemberId(loan.getMemberId().getValue());
        entity.setBookCopyId(loan.getBookCopyId().getValue());
        entity.setStatus(toStatusEntity(loan.getStatus()));
        entity.setDueDate(loan.getDueDate().getValue());
        entity.setReturnedAt(loan.getReturnedAt());

        if (loan.getFineAmount() != null) {
            entity.setFineAmount(loan.getFineAmount().getAmount());
            entity.setFineCurrency(loan.getFineAmount().getCurrency());
        }

        entity.setCreatedAt(loan.getAuditInfo().getCreatedAt());
        entity.setUpdatedAt(loan.getAuditInfo().getUpdatedAt());

        return entity;
    }

    /**
     * Maps JPA entity to domain Loan.
     */
    public static Loan toDomain(JpaLoanEntity entity) {
        FineAmount fineAmount = null;
        if (entity.getFineAmount() != null && entity.getFineCurrency() != null) {
            fineAmount = FineAmount.of(entity.getFineAmount(), entity.getFineCurrency());
        }

        return Loan.reconstitute(
                LoanId.of(entity.getId()),
                MemberId.of(entity.getMemberId()),
                BookCopyId.of(entity.getBookCopyId()),
                toDomainStatus(entity.getStatus()),
                DueDate.of(entity.getDueDate()),
                entity.getReturnedAt(),
                fineAmount,
                AuditInfo.reconstitute(entity.getCreatedAt(), entity.getUpdatedAt()));
    }

    private static JpaLoanEntity.LoanStatusEntity toStatusEntity(LoanStatus status) {
        return switch (status) {
            case OPEN -> JpaLoanEntity.LoanStatusEntity.OPEN;
            case RETURNED -> JpaLoanEntity.LoanStatusEntity.RETURNED;
            case OVERDUE -> JpaLoanEntity.LoanStatusEntity.OVERDUE;
        };
    }

    private static LoanStatus toDomainStatus(JpaLoanEntity.LoanStatusEntity status) {
        return switch (status) {
            case OPEN -> LoanStatus.OPEN;
            case RETURNED -> LoanStatus.RETURNED;
            case OVERDUE -> LoanStatus.OVERDUE;
        };
    }
}
