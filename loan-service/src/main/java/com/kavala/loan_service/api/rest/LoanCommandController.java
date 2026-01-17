package com.kavala.loan_service.api.rest;

import com.kavala.loan_service.api.rest.dto.CheckoutLoanRequest;
import com.kavala.loan_service.api.rest.dto.FineResponse;
import com.kavala.loan_service.api.rest.dto.LoanResponse;
import com.kavala.loan_service.application.command.checkout.CheckoutLoanCommand;
import com.kavala.loan_service.application.command.checkout.CheckoutLoanHandler;
import com.kavala.loan_service.application.command.fine.CalculateFineCommand;
import com.kavala.loan_service.application.command.fine.CalculateFineHandler;
import com.kavala.loan_service.application.command.overdue.MarkLoanOverdueCommand;
import com.kavala.loan_service.application.command.overdue.MarkLoanOverdueHandler;
import com.kavala.loan_service.application.command.returnbook.ReturnLoanCommand;
import com.kavala.loan_service.application.command.returnbook.ReturnLoanHandler;
import com.kavala.loan_service.application.query.get.GetLoanHandler;
import com.kavala.loan_service.application.query.get.GetLoanQuery;
import com.kavala.loan_service.domain.model.FineAmount;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.model.LoanId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

/**
 * REST Controller for loan command operations.
 * Handles checkout, return, overdue, and fine operations.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanCommandController {

    private final CheckoutLoanHandler checkoutLoanHandler;
    private final ReturnLoanHandler returnLoanHandler;
    private final MarkLoanOverdueHandler markLoanOverdueHandler;
    private final CalculateFineHandler calculateFineHandler;
    private final GetLoanHandler getLoanHandler;

    public LoanCommandController(
            CheckoutLoanHandler checkoutLoanHandler,
            ReturnLoanHandler returnLoanHandler,
            MarkLoanOverdueHandler markLoanOverdueHandler,
            CalculateFineHandler calculateFineHandler,
            GetLoanHandler getLoanHandler) {
        this.checkoutLoanHandler = Objects.requireNonNull(checkoutLoanHandler);
        this.returnLoanHandler = Objects.requireNonNull(returnLoanHandler);
        this.markLoanOverdueHandler = Objects.requireNonNull(markLoanOverdueHandler);
        this.calculateFineHandler = Objects.requireNonNull(calculateFineHandler);
        this.getLoanHandler = Objects.requireNonNull(getLoanHandler);
    }

    /**
     * Checkout a book (create a new loan).
     * POST /api/loans/checkout
     */
    @PostMapping("/checkout")
    public ResponseEntity<LoanResponse> checkout(@Valid @RequestBody CheckoutLoanRequest request) {
        CheckoutLoanCommand command = CheckoutLoanCommand.builder()
                .memberId(request.memberId())
                .bookCopyId(request.bookCopyId())
                .loanDays(request.getLoanDays())
                .build();

        LoanId loanId = checkoutLoanHandler.handle(command);

        // Fetch the created loan for response
        Loan loan = getLoanHandler.handle(GetLoanQuery.of(loanId.getValue()))
                .orElseThrow(() -> new IllegalStateException("Loan was created but cannot be found"));

        return ResponseEntity
                .created(URI.create("/api/loans/" + loanId.getValue()))
                .body(LoanResponse.from(loan));
    }

    /**
     * Return a book.
     * POST /api/loans/{id}/return
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponse> returnBook(@PathVariable UUID id) {
        ReturnLoanCommand command = ReturnLoanCommand.of(id);
        returnLoanHandler.handle(command);

        // Fetch the updated loan for response
        Loan loan = getLoanHandler.handle(GetLoanQuery.of(id))
                .orElseThrow(() -> new LoanNotFoundException(id));

        return ResponseEntity.ok(LoanResponse.from(loan));
    }

    /**
     * Mark a loan as overdue.
     * POST /api/loans/{id}/overdue
     */
    @PostMapping("/{id}/overdue")
    public ResponseEntity<LoanResponse> markOverdue(@PathVariable UUID id) {
        MarkLoanOverdueCommand command = MarkLoanOverdueCommand.of(id);
        markLoanOverdueHandler.handle(command);

        // Fetch the updated loan for response
        Loan loan = getLoanHandler.handle(GetLoanQuery.of(id))
                .orElseThrow(() -> new LoanNotFoundException(id));

        return ResponseEntity.ok(LoanResponse.from(loan));
    }

    /**
     * Calculate fine for a loan.
     * POST /api/loans/{id}/fine
     */
    @PostMapping("/{id}/fine")
    public ResponseEntity<FineResponse> calculateFine(@PathVariable UUID id) {
        CalculateFineCommand command = CalculateFineCommand.of(id);
        FineAmount fineAmount = calculateFineHandler.handle(command);

        return ResponseEntity.ok(FineResponse.from(id, fineAmount));
    }

    /**
     * Exception thrown when loan is not found.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class LoanNotFoundException extends RuntimeException {
        public LoanNotFoundException(UUID id) {
            super("Loan not found: " + id);
        }
    }
}
