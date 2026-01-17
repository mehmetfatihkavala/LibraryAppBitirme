package com.kavala.loan_service.domain.event;

import com.kavala.loan_service.domain.model.BookCopyId;
import com.kavala.loan_service.domain.model.FineAmount;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.model.LoanStatus;
import com.kavala.loan_service.domain.model.MemberId;

import java.time.LocalDateTime;

/**
 * Event fired when a loan is returned (book returned to library).
 */
public record LoanReturned(
        LoanId loanId,
        MemberId memberId,
        BookCopyId bookCopyId,
        LoanStatus previousStatus,
        LocalDateTime returnedAt,
        FineAmount fineAmount) implements DomainEvent {

    @Override
    public LocalDateTime occurredAt() {
        return returnedAt;
    }
}
