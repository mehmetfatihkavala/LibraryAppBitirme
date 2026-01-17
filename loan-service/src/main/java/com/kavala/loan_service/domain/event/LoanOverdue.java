package com.kavala.loan_service.domain.event;

import com.kavala.loan_service.domain.model.BookCopyId;
import com.kavala.loan_service.domain.model.DueDate;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.model.MemberId;

import java.time.LocalDateTime;

/**
 * Event fired when a loan becomes overdue.
 */
public record LoanOverdue(
        LoanId loanId,
        MemberId memberId,
        BookCopyId bookCopyId,
        DueDate dueDate,
        long daysOverdue,
        LocalDateTime occurredAt) implements DomainEvent {
}
