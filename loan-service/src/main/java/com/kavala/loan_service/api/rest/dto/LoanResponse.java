package com.kavala.loan_service.api.rest.dto;

import com.kavala.loan_service.domain.model.Loan;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response DTO for basic loan information.
 */
public record LoanResponse(
        UUID id,
        UUID memberId,
        UUID bookCopyId,
        String status,
        LocalDate dueDate,
        boolean isOverdue) {
    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId().getValue(),
                loan.getMemberId().getValue(),
                loan.getBookCopyId().getValue(),
                loan.getStatus().name(),
                loan.getDueDate().getValue(),
                loan.isOverdue());
    }
}
