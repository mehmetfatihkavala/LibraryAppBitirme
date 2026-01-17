package com.kavala.loan_service.domain.event;

import com.kavala.loan_service.domain.model.FineAmount;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.model.MemberId;

import java.time.LocalDateTime;

/**
 * Event fired when a fine is calculated for a loan.
 */
public record FineCalculated(
        LoanId loanId,
        MemberId memberId,
        FineAmount fineAmount,
        long daysOverdue,
        LocalDateTime occurredAt) implements DomainEvent {
}
