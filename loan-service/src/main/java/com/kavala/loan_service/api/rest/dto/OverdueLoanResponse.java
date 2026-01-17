package com.kavala.loan_service.api.rest.dto;

import com.kavala.loan_service.domain.model.Loan;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response DTO for overdue loan information.
 */
public record OverdueLoanResponse(
        UUID id,
        UUID memberId,
        UUID bookCopyId,
        LocalDate dueDate,
        long daysOverdue) {
    public static OverdueLoanResponse from(Loan loan) {
        return new OverdueLoanResponse(
                loan.getId().getValue(),
                loan.getMemberId().getValue(),
                loan.getBookCopyId().getValue(),
                loan.getDueDate().getValue(),
                loan.getDueDate().daysOverdue());
    }
}
