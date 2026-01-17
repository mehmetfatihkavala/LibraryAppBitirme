package com.kavala.loan_service.api.rest.dto;

import com.kavala.loan_service.domain.model.Loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for detailed loan information.
 */
public record LoanDetailResponse(
        UUID id,
        UUID memberId,
        UUID bookCopyId,
        String status,
        LocalDate dueDate,
        boolean isOverdue,
        long daysOverdue,
        LocalDateTime returnedAt,
        BigDecimal fineAmount,
        String fineCurrency,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static LoanDetailResponse from(Loan loan) {
        return new LoanDetailResponse(
                loan.getId().getValue(),
                loan.getMemberId().getValue(),
                loan.getBookCopyId().getValue(),
                loan.getStatus().name(),
                loan.getDueDate().getValue(),
                loan.isOverdue(),
                loan.getDueDate().daysOverdue(),
                loan.getReturnedAt(),
                loan.getFineAmount() != null ? loan.getFineAmount().getAmount() : null,
                loan.getFineAmount() != null ? loan.getFineAmount().getCurrency() : null,
                loan.getAuditInfo().getCreatedAt(),
                loan.getAuditInfo().getUpdatedAt());
    }
}
