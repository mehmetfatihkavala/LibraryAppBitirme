package com.kavala.loan_service.api.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for checking out a book (creating a loan).
 */
public record CheckoutLoanRequest(
        @NotNull(message = "Member ID is required") UUID memberId,

        @NotNull(message = "Book Copy ID is required") UUID bookCopyId,

        @Min(value = 1, message = "Loan days must be at least 1") Integer loanDays) {
    public int getLoanDays() {
        return loanDays != null ? loanDays : 14;
    }
}
