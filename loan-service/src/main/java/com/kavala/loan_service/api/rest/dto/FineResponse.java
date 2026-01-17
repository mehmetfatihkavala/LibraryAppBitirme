package com.kavala.loan_service.api.rest.dto;

import com.kavala.loan_service.domain.model.FineAmount;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response DTO for fine information.
 */
public record FineResponse(
        UUID loanId,
        BigDecimal amount,
        String currency,
        boolean isZero) {
    public static FineResponse from(UUID loanId, FineAmount fineAmount) {
        return new FineResponse(
                loanId,
                fineAmount.getAmount(),
                fineAmount.getCurrency(),
                fineAmount.isZero());
    }
}
