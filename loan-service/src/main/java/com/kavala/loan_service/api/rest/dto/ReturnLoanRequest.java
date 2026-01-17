package com.kavala.loan_service.api.rest.dto;

/**
 * Request DTO for returning a book.
 * Currently empty but can be extended for additional return options.
 */
public record ReturnLoanRequest(
        String notes // Optional notes about the return
) {
}
